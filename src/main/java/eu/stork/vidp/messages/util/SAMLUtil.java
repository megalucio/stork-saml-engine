/*
 * Copyright 2011 by Graz University of Technology, Austria
 * The Austrian STORK Modules have been developed by the E-Government
 * Innovation Center EGIZ, a joint initiative of the Federal Chancellery
 * Austria and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */


/**
 * 
 */
package eu.stork.vidp.messages.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opensaml.Configuration;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import eu.stork.vidp.messages.builder.STORKMessagesBuilder;
import eu.stork.vidp.messages.exception.SAMLException;
import eu.stork.vidp.messages.exception.SAMLValidationException;
import eu.stork.vidp.messages.saml.STORKAttribute;

/**
 * 
 * Helper class for SAML message processing
 * @author bzwattendorfer
 *
 */
public class SAMLUtil {
	
	private final static Logger log = LoggerFactory.getLogger(SAMLUtil.class);
	
	/**
	 * Signs a SAML object
	 * @param samlObject SAML object to sign
	 * @param signingCredential Credentials to be used for signing
	 * @throws SAMLException
	 */
	public static void signSAMLObject(SignableSAMLObject samlObject, Credential signingCredential) throws SAMLException {
		
		log.trace("Signing " + samlObject.getElementQName());
		
		Signature signature = STORKMessagesBuilder.buildXMLObject(Signature.DEFAULT_ELEMENT_NAME);

        signature.setSigningCredential(signingCredential);
        
        //TODO: Make signing algorithm configurable
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
       
        try {
        	//TODO SecurityConfiguration, default signature credentials
            SecurityHelper.prepareSignatureParams(signature, signingCredential, null, null);
        } catch (SecurityException e) {
            throw new SAMLException("Error preparing signature for signing", e);
        }
        
        samlObject.setSignature(signature);

        Marshaller assertionMarshaller = Configuration.getMarshallerFactory().getMarshaller(samlObject);
        try {
            assertionMarshaller.marshall(samlObject);
            Signer.signObject(signature);
        } catch (MarshallingException e) {            
            throw new SAMLException("Unable to marshall " + samlObject.getElementQName() + " for signing", e);
        } catch (SignatureException e) {            
            throw new SAMLException("Unable to sign " + samlObject.getElementQName(), e);
        }
    	
    }
	
	/**
	 * Validated SAML object according the given validation config
	 * @param samlObject SAML object to validaate
	 * @param validatorSuiteConfig Validation config
	 * @throws SAMLValidationException
	 */
	public static void verifySAMLObjectStandardValidation(SignableSAMLObject samlObject, String validatorSuiteConfig) throws SAMLValidationException {
		
		ValidatorSuite validatorSuite = Configuration.getValidatorSuite(validatorSuiteConfig);
		try {
			validatorSuite.validate(samlObject);
		} catch (ValidationException e) {
			log.error(e.getMessage(), e);
			throw new SAMLValidationException("Could not validate " + samlObject.getElementQName(), e);
		}
				
	}
	
	/**
	 * Gets the STORK attribute status from a SAML attribute
	 * @param attribute SAML attribute
	 * @return STORK attribute status
	 */
	public static String getStatusFromAttribute(Attribute attribute) {
		return attribute.getUnknownAttributes().get(STORKAttribute.DEFAULT_STORK_ATTRIBUTE_QNAME);
	}
	
	/**
	 * Gets the XML value of an XML object as String
	 * @param xmlObj XML object
	 * @return XML value as String
	 */
	public static String getStringValueFromXMLObject(XMLObject xmlObj) {
		if (xmlObj instanceof XSString) {
			return ((XSString) xmlObj).getValue();
		} else if (xmlObj instanceof XSAny) {
			return ((XSAny) xmlObj).getTextContent();
		}
		return null;
	}
	
	/**
	 * Gets the attribute value as String of an attribute whereas the attribute is in a given list
	 * @param attrList List of attributes
	 * @param name Name of the attribute where the value should be extracted
	 * @return attribute value as String
	 */
	public static String getAttributeStringValue(List<? extends Attribute> attrList, String name) {		
		XMLObject xmlObj = getAttributeValue(attrList, name);
		return getStringValueFromXMLObject(xmlObj);
	}
	
	/**
	 * Gets the attribute value as String of an attribute
	 * @param attribute Attribute
	 * @return attribute value as String
	 */
	public static String getAttributeStringValue(Attribute attribute) {
		return ((XSString) attribute.getAttributeValues().get(0)).getValue();
	}
	
	/**
	 * Gets the attribute value as anyType of an attribute
	 * @param attribute Attribute
	 * @return value as anyType
	 */
	public static XSAny getAttributeXSAnyValue(Attribute attribute) {
		return (XSAny) attribute.getAttributeValues().get(0);
	}
	
	/**
	 * Gets the attribute value as anyType of an attribute whereas the attribute is in a given list
	 * @param attrList List of attributes
	 * @param name Name of the attribute where the value should be extracted
	 * @return attribute value as anyType
	 */
	public static XSAny getXSAnyAttributeValue(List<Attribute> attrList, String name) {
		//XMLObject xmlObj = getAttributeValue(attrList, name);
		return (XSAny) getAttributeValue(attrList, name);
	}
	
	/**
	 * Gets the attribute value as XMLObject of an attribute whereas the attribute is in a given list
	 * @param attrList List of attributes
	 * @param name Name of the attribute where the value should be extracted
	 * @return attribute value as XMLObject
	 */
	public static XMLObject getAttributeValue(List<? extends Attribute> attrList, String name) {
		Attribute attribute = getAttribute(attrList, name);
		return (attribute != null && !attribute.getAttributeValues().isEmpty()) ? attribute.getAttributeValues().get(0) : null;
	}
	
	/**
	 * Gets the attribute specified by name out of a list
	 * @param attrList List of attributes
	 * @param name attribute name of the attribute to extract
	 * @return attribute
	 */
	public static Attribute getAttribute(List<? extends Attribute> attrList, String name) {
	    for (Attribute attribute : attrList) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
		
        return null;
	}
	
	/**
	 * Gets the attribute specified by name out of a list and immediately removes it from the list
	 * @param attrList List of attributes
	 * @param name attribute name of the attribute to extract and remove
	 * @return attribute
	 */
	public static String getAttributeStringValueAndRemove(List<? extends Attribute> attrList, String name) {
		
		Attribute attribute = getAttribute(attrList, name);
		String value = getAttributeStringValue(attrList, name);
		attrList.remove(attribute);
		
		return value;
	}
	
	/**
	 * Checks if an attribute with a given name is present in a SAML assertion
	 * @param storkAssertion STORK SAML assertion
	 * @param attributeName attribute name
	 * @return true if attribute is present
	 */
	public static boolean containsAttribute(Assertion storkAssertion, String attributeName) {
		AttributeStatement attrStatement = storkAssertion.getAttributeStatements().get(0);
		
		for (Attribute attribute : attrStatement.getAttributes()) {
			if (attribute.getName().equals(attributeName) && (SAMLUtil.getStatusFromAttribute(attribute) == null || SAMLUtil.getStatusFromAttribute(attribute).equals(STORKAttribute.ALLOWED_ATTRIBUTE_STATUS_AVAIL))) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if an attribute with a given name is present in a List of attributes
	 * @param attributeList List of attributes
	 * @param attributeName attribute name
	 * @return true if attribute is present
	 */
	public static boolean containsAttribute(List<? extends Attribute> attributeList, String attributeName) {
		for (Attribute attr : attributeList) {
			if (attr.getName().equals(attributeName))
				return true;
		}
		return false;
	}
	
	/**
	 * Remeoves attribute with a given name from an attribute list
	 * @param attributeList List of attributes
	 * @param attributeName name of the attribute to be removed from list
	 */
	public static void removeAttribute(List<? extends Attribute> attributeList, String attributeName) {
		if (containsAttribute(attributeList, attributeName)) {
			attributeList.remove(getAttribute(attributeList, attributeName));
		}
	}
	
	/**
	 * Gets the String value of an XML object (Only if XMLObject contains String)
	 * @param xmlObj XMLObject
	 * @return String value of XMLObject
	 */
	public static String getXSStringValueFromXMLObject(XMLObject xmlObj) {
		if (xmlObj instanceof XSString)
			return ((XSString) xmlObj).getValue();
			
		return null;
	}
	
	
	/**
	 * Marshalls an XMLObject to an XML element (DOM)
	 * @param message XMLObject
	 * @return DOM representation of XMLObject
	 */
	public static Element marshallMessage(XMLObject message) {        

        try {
            Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(message);
            if (marshaller == null) {
                log.error("Unable to marshall message, no marshaller registered for message object: "
                        + message.getElementQName());
            }
            Element messageElem = marshaller.marshall(message);	            
            return messageElem;
        } catch (MarshallingException e) {
            log.error("Encountered error marshalling message to its DOM representation", e);
            throw new RuntimeException("Encountered error marshalling message into its DOM representation", e);
        }
    }
	
	/**
	 * Unmarshalls a DOM XML element into an OpenSAML XMLObject
	 * @param element DOM element
	 * @return OpenSAML XMLObject
	 * @throws MessageEncodingException
	 */
	public static XMLObject unmarshallMessage(Element element) throws MessageEncodingException {		

        try {
            Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(element);
            if (unmarshaller == null) {
                log.error("Unable to unmarshall element, no unmarshaller registered for message element: "
                        + element.getNodeName());
            }
                       
            return unmarshaller.unmarshall(element);
        } catch (UnmarshallingException e) {
            log.error("Encountered error unmarshalling element to its XMLObject representation", e);
            throw new MessageEncodingException("Encountered error unmarshalling element to its XMLObject representation", e);
        }
	}
	
	/**
	 * Releases the DOM element from an XML document
	 * @param xmlObjList List of XMLObjects to release
	 * @return List of released XMLObjects
	 */
	public static List<? extends XMLObject> releaseDOM(List<? extends XMLObject> xmlObjList) {
		
		List<XMLObject> newXMLObjList = new ArrayList<XMLObject>();
		Iterator<? extends XMLObject> it = xmlObjList.iterator();
		
		while (it.hasNext()) {
			XMLObject xmlObj = it.next();
			xmlObj.detach();
			newXMLObjList.add(xmlObj);
		}
		
		return newXMLObjList;
		
	}
	
	/**
	 * Makes a union of two RequestedAttribute lists (first list has priority and overrides attributes in the second list if equal)
	 * @param priorityList Priority list if attributes might be equal
	 * @param list low priority list
	 * @return Union of both lists
	 */
	public static List<RequestedAttribute> buildRequestedAttributesUnion(List<RequestedAttribute> priorityList, List<RequestedAttribute> list) {
		List<RequestedAttribute> reqAttrList = new ArrayList<RequestedAttribute>();
		
		if (priorityList == null || list == null)
			return reqAttrList;
		
		if (priorityList == null || priorityList.isEmpty()) {
			if (list == null || list.isEmpty()) {
				return reqAttrList;
			} else {
				reqAttrList.addAll((List<RequestedAttribute>) releaseDOM(list));
				return reqAttrList;
			}
		} else {
			if (list == null || list.isEmpty()) {
				reqAttrList.addAll((List<RequestedAttribute>) releaseDOM(priorityList));
				return reqAttrList;
			} else {
				reqAttrList.addAll((List<RequestedAttribute>) releaseDOM(priorityList));
				for (RequestedAttribute reqAttr : list) {
					boolean found = false;
					for (RequestedAttribute prioReqAttr : priorityList) {
						if (!prioReqAttr.getName().equals(reqAttr.getName())) {
							found = true;
						}
					}
					if (!found) {
						reqAttr.detach();
						reqAttrList.add(reqAttr);
						log.debug("Adding additional requested attribute: {} , isRequired: {}", reqAttr.getName(), reqAttr.isRequired());
					}
				}
			}
		}
		
		
		
		return reqAttrList;
	}
	
	
}
