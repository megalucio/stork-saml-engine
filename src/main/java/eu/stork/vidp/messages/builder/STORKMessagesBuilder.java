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
package eu.stork.vidp.messages.builder;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.IdentifierGenerator;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusDetail;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.ws.soap.common.SOAPObject;
import org.opensaml.ws.soap.common.SOAPObjectBuilder;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.stork.mw.messages.saml.STORKAuthnRequest;
import eu.stork.mw.messages.saml.STORKResponse;
import eu.stork.vidp.messages.common.STORKConstants;
import eu.stork.vidp.messages.saml.STORKAttribute;
import eu.stork.vidp.messages.saml.STORKAttributeValue;
import eu.stork.vidp.messages.saml.STORKExtensions;
import eu.stork.vidp.messages.saml.STORKRequestedAttribute;
import eu.stork.vidp.messages.stork.AuthenticationAttributes;
import eu.stork.vidp.messages.stork.CitizenCountryCode;
import eu.stork.vidp.messages.stork.EIDCrossBorderShare;
import eu.stork.vidp.messages.stork.EIDCrossSectorShare;
import eu.stork.vidp.messages.stork.EIDSectorShare;
import eu.stork.vidp.messages.stork.QualityAuthenticationAssuranceLevel;
import eu.stork.vidp.messages.stork.RequestedAttributes;
import eu.stork.vidp.messages.stork.SPAuthRequest;
import eu.stork.vidp.messages.stork.SPCertEnc;
import eu.stork.vidp.messages.stork.SPCertSig;
import eu.stork.vidp.messages.stork.SPCertType;
import eu.stork.vidp.messages.stork.SPID;
import eu.stork.vidp.messages.stork.SPInformation;
import eu.stork.vidp.messages.stork.SpApplication;
import eu.stork.vidp.messages.stork.SpCountry;
import eu.stork.vidp.messages.stork.SpInstitution;
import eu.stork.vidp.messages.stork.SpSector;
import eu.stork.vidp.messages.stork.VIDPAuthenticationAttributes;

/**
 * Class providing several methods for SAML Object generation
 * @author bzwattendorfer
 *
 */
public class STORKMessagesBuilder {
	
	final static Logger log = LoggerFactory.getLogger(STORKMessagesBuilder.class);
	
	/**
	 * Builds an arbitrary OpenSAML XML object
	 * @param <T> OpenSAML XMLObject
	 * @param objectQName QName of the XML element
	 * @return Builded OpenSAML XMLObject 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends XMLObject> T buildXMLObject(QName objectQName) {
		 
		try {
			XMLObjectBuilder<T> builder = (XMLObjectBuilder<T>) Configuration.getBuilderFactory().getBuilder(objectQName);
			return builder.buildObject(objectQName.getNamespaceURI(), objectQName.getLocalPart(), objectQName.getPrefix());			
		} catch (Exception e) {
			log.error("Cannot build XML Object {}: {}", objectQName.getLocalPart(), e);
			throw new RuntimeException(e);
		} 
		
	}
	
	/**
	 * Builds a SOAP object
	 * @param <T> SOAP Object or any extensions
	 * @param objectQName QName of the XML element
	 * @return SOAP Object or any extensions
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SOAPObject> T buildSOAPObject(QName objectQName) {
		 
		try {
			SOAPObjectBuilder<T> builder = (SOAPObjectBuilder<T>) Configuration.getBuilderFactory().getBuilder(objectQName);
			return builder.buildObject();			
		} catch (Exception e) {
			log.error("Cannot build SOAP Object {}: {}", objectQName.getLocalPart(), e);
			throw new RuntimeException(e);
		} 
		
	}

	/**
	 * Builds an arbitrary OpenSAML SAML object
	 * @param <T> OpenSAML SAML Object
	 * @param objectQName QName of the SAML element
	 * @return Builded OpenSAML SAML Object 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends SAMLObject> T buildSAMLObject(QName objectQName) {
		 
		try {
			SAMLObjectBuilder<T> builder = (SAMLObjectBuilder<T>) Configuration.getBuilderFactory().getBuilder(objectQName);
			return builder.buildObject();			
		} catch (Exception e) {
			log.error("Cannot build SAML Object {}: {}", objectQName.getLocalPart(), e);
			throw new RuntimeException(e);
		} 
		
	}
	

	
	/**
	 * Builds SAML Issuer object
	 * @param issuerValue Value for the issuer element
	 * @return Issuer object
	 */
	public static Issuer buildIssuer(String issuerValue) {
		if (StringUtils.isEmpty(issuerValue)) 
			return null;
		
		Issuer issuer = buildXMLObject(Issuer.DEFAULT_ELEMENT_NAME);
		issuer.setValue(issuerValue);
		issuer.setFormat(Issuer.ENTITY);
		
		return issuer;
	}
	
	/**
	 * Builds a QualityAuthenticationAssuranceLevel object
	 * @param qaaValue QAALevel (1 to 4)
	 * @return QualityAuthenticationAssuranceLevel object
	 */
	public static QualityAuthenticationAssuranceLevel buildQualityAuthenticationAssuranceLevel(int qaaValue) {
		if (qaaValue < 1 || qaaValue > 4) {
			log.error("QAA Level must be between 1 and 4.");
			return null;
		}
		
		QualityAuthenticationAssuranceLevel qaaLevel = buildXMLObject(QualityAuthenticationAssuranceLevel.DEFAULT_ELEMENT_NAME);
		qaaLevel.setValue(qaaValue);
		return qaaLevel;			
	}
	
	/**
	 * Builds a STORK RequestedAttribute object
	 * @param name Name of the RequesteAttribute
	 * @param isRequired true or false if RequestedAttribute is required
	 * @param value Value of RequestedAttribute
	 * @return STORK RequestedAttribute object
	 */
	public static RequestedAttribute buildRequestedAttribute(String name, boolean isRequired, String value) {
		
		RequestedAttribute reqAttribute = buildXMLObject(STORKRequestedAttribute.DEFAULT_ELEMENT_NAME);
		reqAttribute.setName(name);
		reqAttribute.setNameFormat(STORKRequestedAttribute.URI_REFERENCE);
		reqAttribute.setIsRequired(isRequired);
		
		if (!StringUtils.isEmpty(value)) {			
			XSString stringValue = buildXSString(STORKAttributeValue.DEFAULT_ELEMENT_NAME);
			stringValue.setValue(value);		
			reqAttribute.getAttributeValues().add(stringValue);
		}
		
		return reqAttribute;			
	}
	
	/**
	 * Builds XML String type object with given QName 
	 * @param qname QName for object to build
	 * @return XML object as String type
	 */
	public static XSString buildXSString(QName qname) {
		XSStringBuilder stringBuilder = (XSStringBuilder) Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
		return stringBuilder.buildObject(qname, XSString.TYPE_NAME);			
	}

	/**
	 * Builds XML Any type object with given QName 
	 * @param qname QName for object to build
	 * @return XML object as Any type
	 */
	public static XSAny buildXSAny(QName qname) {
		XSAnyBuilder anyBuilder = (XSAnyBuilder) Configuration.getBuilderFactory().getBuilder(XSAny.TYPE_NAME);
		return anyBuilder.buildObject(qname, XSAny.TYPE_NAME);			
	}	
	
	/**
	 * Builds a List of RequestedAttribute
	 * @param requestedAttributeArguments RequestedAttributes
	 * @return List of RequestedAttribute
	 */
	public static RequestedAttributes buildRequestedAttributes(RequestedAttribute... requestedAttributeArguments) {
		
		if (requestedAttributeArguments == null)
			return null;
		
		RequestedAttributes reqAttributes = buildXMLObject(RequestedAttributes.DEFAULT_ELEMENT_NAME);
		
		for (RequestedAttribute reqAttr : requestedAttributeArguments) {
			reqAttributes.getRequestedAttributes().add(reqAttr);
		}
		
		return reqAttributes;
	}
	
	/**
	 * Builds RequestedAttributes object out of list of RequestedAttribute
	 * @param requestedAttributeList List of RequestedAttribute
	 * @return RequestedAttributes object
	 */
	public static RequestedAttributes buildRequestedAttributes(List<RequestedAttribute> requestedAttributeList) {
		if (requestedAttributeList == null)
			return null;
		
		RequestedAttributes reqAttributes = buildXMLObject(RequestedAttributes.DEFAULT_ELEMENT_NAME);
		reqAttributes.getRequestedAttributes().addAll(requestedAttributeList);
		
		return reqAttributes;
	}
	
	/**
	 * Builds a STORK CitizenCountryCode object
	 * @param ccc ISO country code
	 * @return CitizenCountryCode object
	 */
	public static CitizenCountryCode buildCitizenCountryCode(String ccc) {
		if (StringUtils.isEmpty(ccc)) {
			log.error("CitizenCountryCode must have a value.");
			return null;
		}
		
		CitizenCountryCode citizenCountryCode = buildXMLObject(CitizenCountryCode.DEFAULT_ELEMENT_NAME);
		citizenCountryCode.setValue(ccc);
		
		return citizenCountryCode;
	}
		
	/**
	 * Builds a SPID object
	 * @param spIDString String to be used as SPID
	 * @return SPID object
	 */
	public static SPID buildSPID(String spIDString) {
		if (StringUtils.isEmpty(spIDString)) {
			log.error("SPID must have a value.");
			return null;
		}
		
		SPID spID = buildXMLObject(SPID.DEFAULT_ELEMENT_NAME);
		spID.setValue(spIDString);
		
		return spID;
	}
	
	/**
	 * Builds SPCertType
	 * @param cert X509Certificate
	 * @return SPCertType
	 */
	private static SPCertType buildSPCertType(X509Certificate cert) {				
		SPCertType spCertType = buildXMLObject(SPCertType.TYPE_NAME); 
		KeyInfo keyInfo = buildXMLObject(KeyInfo.DEFAULT_ELEMENT_NAME);
		X509Data x509DataElem = buildXMLObject(X509Data.DEFAULT_ELEMENT_NAME);
		org.opensaml.xml.signature.X509Certificate x509CertElem = buildXMLObject(org.opensaml.xml.signature.X509Certificate.DEFAULT_ELEMENT_NAME);		
		
		try {
			x509CertElem.setValue(Base64.encodeBytes(cert.getEncoded()));
		} catch (CertificateEncodingException e) {
			log.error("Cannot encode certificate.", e);
			throw new RuntimeException(e);
		}
		
		x509DataElem.getX509Certificates().add(x509CertElem);
		keyInfo.getX509Datas().add(x509DataElem);
		spCertType.setKeyInfo(keyInfo);
		return spCertType;		
	}
	
	/**
	 * Builds SPCertSig object
	 * @param cert X509Certificate
	 * @return SPCertSig
	 */
	public static SPCertSig buildSPCertSig(X509Certificate cert) {
		return (SPCertSig) buildSPCertType(cert);
	}
	
	/**
	 * Builds SPCertEnc object
	 * @param cert X509Certificate
	 * @return SPCertEnc
	 */
	public static SPCertEnc buildSPCertEnc(X509Certificate cert) {
		return (SPCertEnc) buildSPCertType(cert);
	}
	
	/**
	 * Builds SPAuthRequest object
	 * @param xmlObject Abritrary XML object
	 * @return SPAuthRequest
	 */
	public static SPAuthRequest buildSPAuthRequest(XMLObject xmlObject) {		
		SPAuthRequest authRequest = buildXMLObject(SPAuthRequest.DEFAULT_ELEMENT_NAME);
		authRequest.getUnknownXMLObjects().add(xmlObject);
		return authRequest;
	}
	
	/**
	 * Builds SPInformation object
	 * @param spIDString SPID
	 * @param sigCert SP signature certificate
	 * @param encCert SP encryption certificate
	 * @param spAuthRequest Original SP AuthnRequest
	 * @return SPInformations
	 */
	public static SPInformation buildSPInformation(String spIDString, X509Certificate sigCert, X509Certificate encCert, XMLObject spAuthRequest) {
						
		SPInformation spInformation = buildXMLObject(SPInformation.DEFAULT_ELEMENT_NAME);
		
		SPID spID = buildSPID(spIDString);
		spInformation.setSPID(spID);
		
		if (sigCert != null) {
			SPCertSig spCertSig = buildSPCertSig(sigCert);
			spInformation.setSPCertSig(spCertSig);
		}
		
		if (encCert != null) {
			SPCertEnc spCertEnc = buildSPCertEnc(encCert);
			spInformation.setSPCertEnc(spCertEnc);
		}
		
		if (spAuthRequest != null) {
			SPAuthRequest spAuthRequestElem = buildSPAuthRequest(spAuthRequest);
			spInformation.setSPAuthRequest(spAuthRequestElem);
		}
		
		return spInformation;
		
	}
	
	/**
	 * Builds VIDPAuthenticationAttributes objext
	 * @param ccc ISO citizen country code
	 * @param spIDString SPID
	 * @param sigCert SP signature certificate
	 * @param encCert SP encryption certificate
	 * @param spAuthRequest Original SP AuthnRequest
	 * @return VIDPAuthenticationAttributes
	 */
	public static VIDPAuthenticationAttributes buildVIDPAuthenticationAttributes(String ccc, String spIDString, X509Certificate sigCert, X509Certificate encCert, XMLObject spAuthRequest) {
		VIDPAuthenticationAttributes vidpAuthenticationAttributes = buildXMLObject(VIDPAuthenticationAttributes.DEFAULT_ELEMENT_NAME);
		
		CitizenCountryCode citizenCountryCode = buildCitizenCountryCode(ccc);		
		SPInformation spInformation = buildSPInformation(spIDString, sigCert, encCert, spAuthRequest);
		
		vidpAuthenticationAttributes.setCitizenCountryCode(citizenCountryCode);		
		vidpAuthenticationAttributes.setSPInformation(spInformation);
		
		return vidpAuthenticationAttributes;
	}
	
	/**
	 * Builds AuthenticationAttributes object
	 * @param ccc ISO citizen country code
	 * @param spIDString SPID
	 * @param sigCert SP signature certificate
	 * @param encCert SP encryption certificate
	 * @param spAuthRequest Original SP AuthnRequest
	 * @return AuthenticationAttributes
	 */
	public static AuthenticationAttributes buildAuthenticationAttributes(String ccc, String spIDString, X509Certificate sigCert, X509Certificate encCert, XMLObject spAuthRequest) {
		AuthenticationAttributes authenticationAttributes = buildXMLObject(AuthenticationAttributes.DEFAULT_ELEMENT_NAME);
		
		VIDPAuthenticationAttributes vidpAuthenticationAttributes = buildVIDPAuthenticationAttributes(ccc, spIDString, sigCert, encCert, spAuthRequest);
		
		authenticationAttributes.setVIDPAuthenticationAttributes(vidpAuthenticationAttributes);
		return authenticationAttributes;
		
	}
	
	/**
	 * Builds SpSector object
	 * @param spSector Value SPSector value
	 * @return SpSector
	 */
	public static SpSector buildSpSector(String spSectorValue) {
			
		SpSector spSector = buildXMLObject(SpSector.DEFAULT_ELEMENT_NAME);
		spSector.setValue(spSectorValue);
		
		return spSector;
	}
	
	/**
	 * Builds SpInstitution object
	 * @param spInstitutionValue Value for SpInstitution
	 * @return SpInstitution
	 */
	public static SpInstitution buildSpInstitution(String spInstitutionValue) {
		
		SpInstitution spInstitution = buildXMLObject(SpInstitution.DEFAULT_ELEMENT_NAME);
		spInstitution.setValue(spInstitutionValue);
		
		return spInstitution;
	}
	
	
	/**
	 * Builds SpApplication object
	 * @param spApplicationValue Value for SpApplication
	 * @return SpApplication
	 */
	public static SpApplication buildSpApplication(String spApplicationValue) {
		
		SpApplication spApplication = buildXMLObject(SpApplication.DEFAULT_ELEMENT_NAME);
		spApplication.setValue(spApplicationValue);
		
		return spApplication;
	}
	
	/**
	 * Builds SpCountry object
	 * @param spCountryValue ISO Code Value for SpCountry
	 * @return SpCountry
	 */
	public static SpCountry buildSpCountry(String spCountryValue) {
		
		SpCountry spCountry = buildXMLObject(SpCountry.DEFAULT_ELEMENT_NAME);
		spCountry.setValue(spCountryValue);
		
		return spCountry;
	}
		
	/**
	 * Generates secured randomized ID for SAML Messages
	 * @return secured randomized ID
	 */
	public static String generateID() {
		try {
			IdentifierGenerator idGenerator = new SecureRandomIdentifierGenerator();
			return idGenerator.generateIdentifier();
		} catch (NoSuchAlgorithmException e) {
			log.error("Cannot generate id", e);
			throw new RuntimeException(e);
			
		}
		
	}
	
	/**
	 * Builds STORKAuthnRequest object
	 * @param destination Endpoint for AuthnRequest
	 * @param acsURL Endpoint where STORK response wants to be received
	 * @param providerName Provider Name
	 * @param issuerValue Value for Issuer element
	 * @param qaaLevel STORK QAALevel
	 * @param requestedAttributes Attributes to be requested
	 * @param spSector SPSector
	 * @param spInstitution SPInstitution
	 * @param spApplication SPApplication
	 * @param spCountry SPCountry
	 * @return STORKAuthnRequest
	 */
	public static STORKAuthnRequest buildSTORKAuthnRequest(
			String destination,
			String acsURL,
			String providerName,
			String issuerValue,
			QualityAuthenticationAssuranceLevel qaaLevel,
			RequestedAttributes requestedAttributes,
			String spSector,
			String spInstitution,
			String spApplication,
			String spCountry) {
		
		//fixed values
		String consent = STORKAuthnRequest.UNSPECIFIED_CONSENT;
		boolean forceAuthn = true;
		boolean isPassive = false;
		String binding = SAMLConstants.SAML2_POST_BINDING_URI;
		boolean eIDSectorShare = true;
		boolean eIDCrossSectorShare = true;
		boolean eIDCrossBorderShare = false;
		
		STORKAuthnRequest authnRequest = buildXMLObject(STORKAuthnRequest.DEFAULT_ELEMENT_NAME);
		
		authnRequest.setVersion(SAMLVersion.VERSION_20);
		authnRequest.setID(generateID());
		authnRequest.setIssueInstant(new DateTime());
		
		authnRequest.setConsent(consent);
		authnRequest.setForceAuthn(forceAuthn);
		authnRequest.setIsPassive(isPassive);
		authnRequest.setProtocolBinding(binding);
		
		authnRequest.setDestination(destination);
		authnRequest.setAssertionConsumerServiceURL(acsURL);
		authnRequest.setProviderName(providerName);
		authnRequest.setIssuer(buildIssuer(issuerValue));
		
		STORKExtensions extensions = buildSTORKExtensions();
		
		authnRequest.setQAALevel(qaaLevel.getValue());
		extensions.setQAALevel(qaaLevel);
		
		authnRequest.setRequestedAttributes(requestedAttributes.getRequestedAttributes());
		extensions.setRequestedAttributes(requestedAttributes);
						
		EIDSectorShare eidSectorShareObj = buildXMLObject(EIDSectorShare.DEFAULT_ELEMENT_NAME);
		eidSectorShareObj.setValue(eIDSectorShare);
		
		EIDCrossSectorShare eidCrossSectorShareObj = buildXMLObject(EIDCrossSectorShare.DEFAULT_ELEMENT_NAME);
		eidCrossSectorShareObj.setValue(eIDCrossSectorShare);
		
		EIDCrossBorderShare eidCrossBorderShareObj = buildXMLObject(EIDCrossBorderShare.DEFAULT_ELEMENT_NAME);
		eidCrossBorderShareObj.setValue(eIDCrossBorderShare);
		
		SpSector spSectorObj = buildSpSector(spSector);		
		SpInstitution spInstitutionObj = buildSpInstitution(spInstitution);
		SpApplication spApplicationObj = buildSpApplication(spApplication);
		SpCountry spCountryObj = buildSpCountry(spCountry);

				
		extensions.getUnknownXMLObjects().add(qaaLevel);
		extensions.getUnknownXMLObjects().add(spSectorObj);
		extensions.getUnknownXMLObjects().add(spInstitutionObj);		
		extensions.getUnknownXMLObjects().add(spApplicationObj);
		extensions.getUnknownXMLObjects().add(spCountryObj);
		extensions.getUnknownXMLObjects().add(eidSectorShareObj);
		extensions.getUnknownXMLObjects().add(eidCrossSectorShareObj);
		extensions.getUnknownXMLObjects().add(eidCrossBorderShareObj);
		extensions.getUnknownXMLObjects().add(requestedAttributes);		
				
		authnRequest.setExtensions(extensions);
		
		return authnRequest;
	}
	
	/**
	 * Builds STORKAuthnRequest object
	 * @param destination Endpoint for AuthnRequest
	 * @param acsURL Endpoint where STORK response wants to be received
	 * @param providerName Provider Name
	 * @param issuerValue Value for Issuer element
	 * @param qaaLevel STORK QAALevel
	 * @param requestedAttributeList List of STORK attributes to be requested
	 * @param ccc ISO citizen country code
	 * @param spIDString SPID
	 * @param sigCert SP signature certificate
	 * @param encCert SP encryption certificate
	 * @param spAuthRequest Original SP AuthnRequest
	 * @param spSector SPSector
	 * @param spInstitution SPInstitution
	 * @param spApplication SPApplication
	 * @param spCountry SPCountry
	 * @return STORKAuthnRequest
	 */
	public static STORKAuthnRequest buildSTORKAuthnRequest(
			String destination,
			String acsURL, 
			String providerName,
			String issuerValue,
			int qaaLevel,			
			List<RequestedAttribute> requestedAttributeList,
			String ccc,			
			String spID,
			X509Certificate sigCert,
			X509Certificate encCert,
			XMLObject spAuthRequest,
			String spSector,
			String spInstitution,
			String spApplication,
			String spCountry) {
		
		//fixed values via config
		String consent = STORKAuthnRequest.UNSPECIFIED_CONSENT;
		boolean forceAuthn = true;
		boolean isPassive = false;
		String binding = SAMLConstants.SAML2_POST_BINDING_URI;
		boolean eIDSectorShare = true;
		boolean eIDCrossSectorShare = true;
		boolean eIDCrossBorderShare = false;
		
		return buildSTORKAuthnRequest(consent, forceAuthn, isPassive, binding, eIDSectorShare, eIDCrossSectorShare, eIDCrossBorderShare, destination, acsURL, providerName, issuerValue, qaaLevel, requestedAttributeList, ccc, spID, sigCert, encCert, spAuthRequest, spSector, spInstitution, spApplication, spCountry);
		
	}
	
	/**
	 * Builds STORKAuthnRequest object
	 * @param consent Consent for the request
	 * @param forceAuthn forceAuthn
	 * @param isPassive isPassive
	 * @param binding Binding the request is sent over
	 * @param eIDSectorShare Should eIdentifier be shared?
	 * @param eIDCrossSectorShare Should eIdentifier be shared across sectors?
	 * @param eIDCrossBorderShare Should eIdentifier be shared across borders?
	 * @param destination Endpoint for AuthnRequest
	 * @param acsURL Endpoint where STORK response wants to be received
	 * @param providerName Provider Name
	 * @param issuerValue Value for Issuer element
	 * @param qaaLevel STORK QAALevel
	 * @param requestedAttributeList List of STORK attributes to be requested
	 * @param ccc ISO citizen country code
	 * @param spIDString SPID
	 * @param sigCert SP signature certificate
	 * @param encCert SP encryption certificate
	 * @param spAuthRequest Original SP AuthnRequest
	 * @param spSector SPSector
	 * @param spInstitution SPInstitution
	 * @param spApplication SPApplication
	 * @param spCountry SPCountry
	 * @return STORKAuthnRequest
	 */
	public static STORKAuthnRequest buildSTORKAuthnRequest(
			String consent,
			boolean forceAuthn,
			boolean isPassive,
			String binding,
			boolean eIDSectorShare,
			boolean eIDCrossSectorShare,
			boolean eIDCrossBorderShare,			
			String destination,
			String acsURL, 
			String providerName,
			String issuerValue,
			int qaaLevel,
			List<RequestedAttribute> requestedAttributeList,
			String ccc,			
			String spID,
			X509Certificate sigCert,
			X509Certificate encCert,
			XMLObject spAuthRequest, 
			String spSector,
			String spInstitution,
			String spApplication,
			String spCountry) {
		
		STORKAuthnRequest authnRequest = buildXMLObject(STORKAuthnRequest.DEFAULT_ELEMENT_NAME);
		
		authnRequest.setVersion(SAMLVersion.VERSION_20);
		authnRequest.setID(generateID());
		authnRequest.setIssueInstant(new DateTime());
		
		authnRequest.setDestination(destination);
		authnRequest.setAssertionConsumerServiceURL(acsURL);
		authnRequest.setProviderName(providerName);
		authnRequest.setIssuer(buildIssuer(issuerValue));
		authnRequest.setQAALevel(qaaLevel);
		authnRequest.setRequestedAttributes(requestedAttributeList);
		authnRequest.setCitizenCountryCode(ccc);		
		authnRequest.setSPID(spID);
		authnRequest.setSPCertSig(sigCert);
		authnRequest.setSPCertEnc(encCert);
		authnRequest.setOriginalSPAuthRequest(spAuthRequest);
		
		authnRequest.setConsent(consent);
		authnRequest.setForceAuthn(forceAuthn);
		authnRequest.setIsPassive(isPassive);
		authnRequest.setProtocolBinding(binding);
		
		addSTORKExtensionsToAuthnRequest(authnRequest, qaaLevel, requestedAttributeList, ccc, spID, sigCert, encCert, spAuthRequest, eIDSectorShare, eIDCrossSectorShare, eIDCrossBorderShare, spSector, spInstitution, spApplication, spCountry);
		
		return authnRequest;
		
	}
	
	/**
	 * Adds STORK Extensions to STORKAuthnRequest
	 * @param authnRequest
	 * @param qaaLevel STORK QAALevel
	 * @param requestedAttributeList List of STORK attributes to be requested
	 * @param ccc ISO citizen country code
	 * @param spIDString SPID
	 * @param sigCert SP signature certificate
	 * @param encCert SP encryption certificate
	 * @param spAuthRequest Original SP AuthnRequest
	 * @param spSector SPSector
	 * @param spInstitution SPInstitution
	 * @param spApplication SPApplication
	 * @param spCountry SPCountry
	 */
	public static void addSTORKExtensionsToAuthnRequest(
			STORKAuthnRequest authnRequest,
			int qaaLevel, 
			List<RequestedAttribute> requestedAttributeList, 
			String ccc,			
			String spID,
			X509Certificate sigCert,
			X509Certificate encCert,
			XMLObject spAuthRequest,
			boolean eIDSectorShare,
			boolean eIDCrossSectorShare,
			boolean eIDCrossBorderShare, 
			String spSector, 
			String spInstitution, 
			String spApplication, 
			String spCountry) {
				
		STORKExtensions extensions = buildSTORKExtensions();
		authnRequest.setRequestedAttributes(requestedAttributeList);
		
		QualityAuthenticationAssuranceLevel qaaLevelObj = buildQualityAuthenticationAssuranceLevel(qaaLevel);
		RequestedAttributes requestedAttributesObj = buildRequestedAttributes(requestedAttributeList);
		AuthenticationAttributes authenticationAttributesObj = buildAuthenticationAttributes(ccc, spID, sigCert, encCert, spAuthRequest);
		
		EIDSectorShare eidSectorShareObj = buildXMLObject(EIDSectorShare.DEFAULT_ELEMENT_NAME);
		eidSectorShareObj.setValue(eIDSectorShare);
		
		EIDCrossSectorShare eidCrossSectorShareObj = buildXMLObject(EIDCrossSectorShare.DEFAULT_ELEMENT_NAME);
		eidCrossSectorShareObj.setValue(eIDCrossSectorShare);
		
		EIDCrossBorderShare eidCrossBorderShareObj = buildXMLObject(EIDCrossBorderShare.DEFAULT_ELEMENT_NAME);
		eidCrossBorderShareObj.setValue(eIDCrossBorderShare);
		
		SpSector spSectorObj = buildSpSector(spSector);		
		SpApplication spApplicationObj = buildSpApplication(spApplication);
		SpCountry spCountryObj = buildSpCountry(spCountry);

		extensions.setQAALevel(qaaLevelObj);
		extensions.setRequestedAttributes(requestedAttributesObj);
		extensions.setAuthenticationAttributes(authenticationAttributesObj);
		
		extensions.getUnknownXMLObjects().add(qaaLevelObj);
		extensions.getUnknownXMLObjects().add(spSectorObj);		
		extensions.getUnknownXMLObjects().add(spApplicationObj);
		extensions.getUnknownXMLObjects().add(spCountryObj);
		extensions.getUnknownXMLObjects().add(eidSectorShareObj);
		extensions.getUnknownXMLObjects().add(eidCrossSectorShareObj);
		extensions.getUnknownXMLObjects().add(eidCrossBorderShareObj);
		extensions.getUnknownXMLObjects().add(requestedAttributesObj);
		extensions.getUnknownXMLObjects().add(authenticationAttributesObj);
				
		authnRequest.setExtensions(extensions);
				
	}
	
	
	/**
	 * Builds STORKExtensions object
	 * @return STORKExtensions
	 */
	public static STORKExtensions buildSTORKExtensions() {
		QName samlProtocolExtensions = new QName(SAMLConstants.SAML20P_NS, STORKExtensions.LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
		return buildXMLObject(samlProtocolExtensions);
	}
	
	/**
	 * Builds STORKResponse
	 * @param destination Endpoint where the STORKResponse should be sent to
	 * @param inResponseTo ID of the corresponding AuthnRequest
	 * @param issuer Issuer value of the response
	 * @param status Status of the response (success, error, etc.)
	 * @param assertion SAML assertion to be included
	 * @return STORKResponse
	 */
	public static STORKResponse buildSTORKResponse(
			String destination,
			String inResponseTo,
			Issuer issuer,
			Status status,
			Assertion assertion) {
		
		STORKResponse response = buildXMLObject(STORKResponse.DEFAULT_ELEMENT_NAME);
		
		response.setDestination(destination);
		response.setInResponseTo(inResponseTo);
		response.setConsent(STORKResponse.OBTAINED_CONSENT);
		response.setID(generateID());
		response.setIssueInstant(new DateTime());
		response.setVersion(SAMLVersion.VERSION_20);
		
		response.setIssuer(issuer);
		response.setStatus(status);
		response.getAssertions().add(assertion);
		
		return response;		
	}
	
	/**
	 * Build STORKResponse
	 * @param destination Endpoint where the STORKResponse should be sent to
	 * @param inResponseTo ID of the corresponding AuthnRequest
	 * @param issuer Issuer value of the response
	 * @param status Status of the response (success, error, etc.)
	 * @param statusMessage Status message for the response
	 * @param assertion SAML assertion to be included
	 * @return STORKResponse
	 */
	public static STORKResponse buildSTORKResponse(
			String destination,
			String inResponseTo,
			String issuerString,
			String statusCode,
			String statusMessage,
			Assertion assertion) {
		
		Status status = buildStatus(statusCode, statusMessage);
		Issuer issuer = buildIssuer(issuerString);
		
		return buildSTORKResponse(destination, inResponseTo, issuer, status, assertion);
	}
	
	
	/**
	 * Builds a STORKResponse containing no assertion
	 * @param destination Endpoint where the STORKResponse should be sent to
	 * @param inResponseTo ID of the corresponding AuthnRequest
	 * @param issuer Issuer value of the response
	 * @param status Status of the response (success, error, etc.)
	 * @param statusMessage Status message for the response
	 * @return STORKResponse
	 */
	public static STORKResponse buildSTORKErrorResponse(
			String destination,
			String inResponseTo,
			String issuerString,
			String statusCode,
			String statusMessage) {
		
		return buildSTORKResponse(destination, inResponseTo, issuerString, statusCode, statusMessage, null);
	}
	
	/**
	 * Builds Status object
	 * @param statusCodeValue StatusCode
	 * @param statusMessageValue StatusMessage
	 * @return Status
	 */
	public static Status buildStatus(String statusCodeValue, String statusMessageValue) {
		return buildStatus(statusCodeValue, statusMessageValue, null);
	}
	
	/**
	 * Builds Status object
	 * @param statusCodeValue StatusCode
	 * @param statusMessageValue StatusMessage
	 * @param detail Detail Message
	 * @return Status
	 */
	public static Status buildStatus(String statusCodeValue, String statusMessageValue, XMLObject detail) {
		StatusCode statusCode = buildXMLObject(StatusCode.DEFAULT_ELEMENT_NAME);
		statusCode.setValue(statusCodeValue);
		
		StatusMessage statusMessage = buildXMLObject(StatusMessage.DEFAULT_ELEMENT_NAME);
		statusMessage.setMessage(statusMessageValue);
		
		StatusDetail statusDetail = buildXMLObject(StatusDetail.DEFAULT_ELEMENT_NAME);
		statusDetail.getUnknownXMLObjects().add(detail);
		
		Status status = buildXMLObject(Status.DEFAULT_ELEMENT_NAME);
		status.setStatusCode(statusCode);
		status.setStatusMessage(statusMessage);
		
		return status;
		
	}
	
	/**
	 * Builds Assertion
	 * @param issuer Issuer value for assertion
	 * @param subject Subject of assertion
	 * @param conditions Conditions of assertion
	 * @param authnStatement AuthnStatement
	 * @param attributeStatement AttributeAtatement
	 * @return Assertion
	 */
	public static Assertion buildAssertion(Issuer issuer, 
			Subject subject, 
			Conditions conditions,
			AuthnStatement authnStatement,
			AttributeStatement attributeStatement) {
		Assertion assertion = buildXMLObject(Assertion.DEFAULT_ELEMENT_NAME);
		
		assertion.setID(generateID());
		assertion.setVersion(SAMLVersion.VERSION_20);
		assertion.setIssueInstant(new DateTime());
		
		assertion.setIssuer(issuer);
		assertion.setSubject(subject);
		assertion.setConditions(conditions);
		assertion.getAuthnStatements().add(authnStatement);
		assertion.getAttributeStatements().add(attributeStatement);
		
		return assertion;
	}
	
	/**
	 * Builds Assertion object
	 * @param issuerValue Value of the issuer
	 * @param nameQualifier nameQualifier
	 * @param spNameQualifier spNameQualifier
	 * @param spProviderID spProviderID
	 * @param ipAddress IP address of the client
	 * @param inResponseTo ID of the corresponding AuthnRequest
	 * @param notBefore Time before assertion is not valid
	 * @param notOnOrAfter Time after assertion is not valid
	 * @param recipient Recipient of the assertion
	 * @param attributeList Attributes to be included in the assertion
	 * @return Assertion
	 */
	public static Assertion buildAssertion(
			String issuerValue,
			String nameQualifier,
			String spNameQualifier,
			String spProviderID,
			String ipAddress,
			String inResponseTo,
			DateTime notBefore,
			DateTime notOnOrAfter,
			String recipient,
			List<Attribute> attributeList) {
		
		Issuer issuer = buildIssuer(issuerValue);
		NameID nameID = buildNameID(NameID.UNSPECIFIED, nameQualifier, spNameQualifier, spProviderID, NameID.UNSPECIFIED);
		SubjectConfirmationData scData = buildSubjectConfirmationData(ipAddress, inResponseTo, notOnOrAfter, recipient);
		SubjectConfirmation subjectConfirmation = buildSubjectConfirmation(SubjectConfirmation.METHOD_BEARER, scData);
		Subject subject = buildSubject(nameID, subjectConfirmation);
		
		List<Audience> audienceList = buildAudienceList(recipient);
		Conditions conditions = buildConditions(notBefore, notOnOrAfter, audienceList);
		AuthnStatement authnStatement = buildAuthnStatement(ipAddress);
		AttributeStatement attributeStatement = buildAttributeStatement(attributeList);		
		
		return buildAssertion(issuer, subject, conditions, authnStatement, attributeStatement);
	}
	
	/**
	 * Builds List of Audience objects
	 * @param audiences Audience strings
	 * @return List of Audience
	 */
	public static List<Audience> buildAudienceList(String... audiences) {
		List<Audience> audienceList = new ArrayList<Audience>();
		
		for (String audienceString : audiences) {
			Audience audience = buildXMLObject(Audience.DEFAULT_ELEMENT_NAME);
			audience.setAudienceURI(audienceString);
			audienceList.add(audience);
		}
		
		return audienceList;
	}
	
	/**
	 * Builds NameID object
	 * @param format Format of the NameID
	 * @param nameQualifier nameQualifier
	 * @param spNameQualifier spNameQualifier
	 * @param spProviderID spProviderID
	 * @param value Value of the NameID
	 * @return NameID
	 */
	public static NameID buildNameID(String format, 
			String nameQualifier,
			String spNameQualifier,
			String spProviderID,
			String value) {
		
		NameID nameID = buildXMLObject(NameID.DEFAULT_ELEMENT_NAME);
		
		nameID.setFormat(format);
		nameID.setNameQualifier(nameQualifier);
		nameID.setSPNameQualifier(spNameQualifier);
		nameID.setSPProvidedID(spProviderID);
		nameID.setValue(value);
		
		return nameID;
		
	}
	
	/**
	 * Builds SubjectConfirmation object
	 * @param method Method of SubjectConfirmation
	 * @param scData SubjectConfirmationData
	 * @return SubjectConfirmation
	 */
	public static SubjectConfirmation buildSubjectConfirmation(String method, SubjectConfirmationData scData) {
		
		SubjectConfirmation subjectConfirmation = buildXMLObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
		
		subjectConfirmation.setMethod(method);
		subjectConfirmation.setSubjectConfirmationData(scData);
		
		return subjectConfirmation;
	}
	
	/**
	 * Builds SubjectConfirmationData object
	 * @param ipAddress IP address of the client
	 * @param inResponseTo ID of the corresponding AuthnRequest
	 * @param notOnOrAfter Time after subject is not valid
	 * @param recipient recipient of the assertion
	 * @return SubjectConfirmationData
	 */
	public static SubjectConfirmationData buildSubjectConfirmationData(String ipAddress, 
			String inResponseTo, 
			DateTime notOnOrAfter, 
			String recipient) {
		
		SubjectConfirmationData scData =  buildXMLObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
		
		scData.setAddress(ipAddress);
		scData.setInResponseTo(inResponseTo);
		scData.setNotOnOrAfter(notOnOrAfter);
		scData.setRecipient(recipient);
		
		return scData;
		
	}
	
	/**
	 * Builds Subject object
	 * @param nameID NameID object
	 * @param subjectConfirmation SubjectConfirmation
	 * @return Subject
	 */
	public static Subject buildSubject(NameID nameID, SubjectConfirmation subjectConfirmation) {
		
		Subject subject = buildXMLObject(Subject.DEFAULT_ELEMENT_NAME);
		subject.setNameID(nameID);
		subject.getSubjectConfirmations().add(subjectConfirmation);
		
		return subject;
	}
	
	/**
	 * Build Conditions object
	 * @param notBefore Time before assertion is not valid
	 * @param notOnOrAfter Time after assertion is not valid
	 * @param audienceList List of audience
	 * @return Conditions
	 */
	public static Conditions buildConditions(DateTime notBefore, DateTime notOnOrAfter, List<Audience> audienceList) {
		Conditions conditions = buildXMLObject(Conditions.DEFAULT_ELEMENT_NAME);
		
		conditions.setNotBefore(notBefore);
		conditions.setNotOnOrAfter(notOnOrAfter);
		
		AudienceRestriction audienceRestriction = buildXMLObject(AudienceRestriction.DEFAULT_ELEMENT_NAME);
		audienceRestriction.getAudiences().addAll(audienceList);
		conditions.getAudienceRestrictions().add(audienceRestriction);

		OneTimeUse oneTimeUse = buildXMLObject(OneTimeUse.DEFAULT_ELEMENT_NAME);
		conditions.getConditions().add(oneTimeUse);
		
		return conditions;
		
	}
	
	/**
	 * Build AuthnStatement object
	 * @param authInstant Time instant of authentication
	 * @param subjectLocality subjectLocality
	 * @param authnContext AuthnContext used
	 * @return AuthnStatement
	 */
	public static AuthnStatement buildAuthnStatement(DateTime authInstant, SubjectLocality subjectLocality, AuthnContext authnContext) {
		AuthnStatement authnStatement = buildXMLObject(AuthnStatement.DEFAULT_ELEMENT_NAME);
		
		authnStatement.setAuthnInstant(authInstant);
		authnStatement.setSubjectLocality(subjectLocality);
		authnStatement.setAuthnContext(authnContext);
		
		return authnStatement;
	}
	
	/**
	 * Build AuthnStatement object
	 * @param ipAddress IP address of the client
	 * @return AuthnStatement
	 */
	public static AuthnStatement buildAuthnStatement(String ipAddress) {
		AuthnStatement authnStatement = buildXMLObject(AuthnStatement.DEFAULT_ELEMENT_NAME);
		
		authnStatement.setAuthnInstant(new DateTime());
		
		SubjectLocality subjectLocality = buildXMLObject(SubjectLocality.DEFAULT_ELEMENT_NAME);
		subjectLocality.setAddress(ipAddress);
		authnStatement.setSubjectLocality(subjectLocality);
		
		AuthnContext authnContext = buildXMLObject(AuthnContext.DEFAULT_ELEMENT_NAME);
		authnStatement.setAuthnContext(authnContext);
		
		return authnStatement;
	}
	
	/**
	 * Builds AttributeStatement object
	 * @return AttributeStatement
	 */
	public static AttributeStatement buildAttributeStatement() {
		return buildXMLObject(AttributeStatement.DEFAULT_ELEMENT_NAME);
	}
	
	/**
	 * Builds AttributeStatement object
	 * @param attributeList List of attributes
	 * @return AttributeStatement
	 */
	public static AttributeStatement buildAttributeStatement(List<Attribute> attributeList) {
		AttributeStatement attributeStatement = buildXMLObject(AttributeStatement.DEFAULT_ELEMENT_NAME);
		attributeStatement.getAttributes().addAll(attributeList);
		
		return attributeStatement;
	}
	
	/**
	 * Builds STORK String Attribute
	 * @param name Attribute Name
	 * @param friendlyName friendlyName of Attribute
	 * @param value Value of Attribute
	 * @param status STORK status of attribute
	 * @return STORK String Attribute
	 */
	public static Attribute buildSTORKStringAttribute(String name, String friendlyName, String value, String status) {
		XSString xsString = buildXSString(AttributeValue.DEFAULT_ELEMENT_NAME);
		xsString.setValue(value);
				
		return buildAttribute(name, friendlyName, status, xsString);
	}
	
	/**
	 * Builds STORK XML Any Attribute
	 * @param name Attribute Name
	 * @param friendlyName friendlyName of Attribute
	 * @param value Value of Attribute
	 * @param status STORK status of attribute
	 * @return STORK XML Any Attribute
	 */
	public static Attribute buildSTORKXMLAttribute(String name, String friendlyName, XMLObject value, String status) {
		XSAny xsAny = buildXMLObject(AttributeValue.DEFAULT_ELEMENT_NAME);
		xsAny.getUnknownXMLObjects().add(value);
		
		return buildAttribute(name, friendlyName, status, xsAny);
	}
	
	/**
	 * Builds STORK Attribute
	 * @param name Attribute Name
	 * @param friendlyName friendlyName of Attribute
	 * @param status STORK status of Attribute
	 * @param attributeValue Value of the Attribute
	 * @return Attribute
	 */
	public static Attribute buildAttribute(String name, String friendlyName, String status, XMLObject attributeValue) {
		Attribute attribute = buildXMLObject(STORKAttribute.DEFAULT_ELEMENT_NAME);
		
		attribute.setNameFormat(STORKAttribute.URI_REFERENCE);
		attribute.setFriendlyName(friendlyName);
		attribute.setName(name);
		attribute.getUnknownAttributes().put(STORKAttribute.DEFAULT_STORK_ATTRIBUTE_QNAME, status);
		attribute.getAttributeValues().add(attributeValue);
		
		return attribute;
	}
	
	/**
	 * Builds STORK String Attribute
	 * @param name Attribute Name
	 * @param friendlyName friendlyName of Attribute
	 * @param status STORK status of Attribute
	 * @param attributeValue Value of the Attribute
	 * @return String Attribute
	 */
	public static Attribute buildStringAttribute(String name, String friendlyName, String status, String attributeValue) {
		Attribute attribute = buildXMLObject(STORKAttribute.DEFAULT_ELEMENT_NAME);
		
		attribute.setNameFormat(STORKAttribute.URI_REFERENCE);
		attribute.setFriendlyName(friendlyName);
		attribute.setName(name);
		attribute.getUnknownAttributes().put(STORKAttribute.DEFAULT_STORK_ATTRIBUTE_QNAME, status);
		
		XSString xsString = buildXSString(AttributeValue.DEFAULT_ELEMENT_NAME);
		xsString.setValue(attributeValue);
		attribute.getAttributeValues().add(xsString);
		
		return attribute;
	}
	
	/**
	 * Builds DSS signature request
	 * @param textToBeSigned Text to be included in the DSS request
	 * @param mimeType MimeType of the contents
	 * @return DSS signature request as String
	 */
	public static String buildSignatureRequestString(String textToBeSigned, String mimeType) {
		//MimeType=\"text/plain\"
		//MimeType=\"application/xhtml+xml\"
		String sigRequestString = 
			"<dss:SignRequest xmlns:dss=\"urn:oasis:names:tc:dss:1.0:core:schema\" " + 
				"RequestID=\"" + generateID() + "\">" +
				"<dss:InputDocuments>" + 
					"<dss:Document>" +
					"<dss:Base64Data MimeType=\"" + mimeType + "\">" + Base64.encodeBytes(textToBeSigned.getBytes()) + "</dss:Base64Data>" +
					"</dss:Document>" + 
				"</dss:InputDocuments>" +
			"</dss:SignRequest>";
	
		return sigRequestString;
		
	}
	
	/**
	 * Builds STORK signedDoc RequestedAttribute
	 * @param textToBeSigned Text to be included in the DSS request
	 * @param mimeType MimeType of the contents
	 * @param isRequired true or false if signedDoc RequestedAttribute is required
	 * @return STORK signedDoc RequestedAttribute
	 */
	public static RequestedAttribute buildSignatureRequestRequestedAttribute(String textToBeSigned, String mimeType, boolean isRequired) {
		return buildRequestedAttribute(STORKConstants.STORK_ATTRIBUTE_SIGNEDDOC, isRequired, buildSignatureRequestString(textToBeSigned, mimeType));
	}
	
	/**
	 * Adds RequestedAttribute to STORKAuthnRequest
	 * @param authnRequest STORKAuthnRequest
	 * @param reqAttr RequestedAttribute
	 */
	public static void addRequestedAttribute(STORKAuthnRequest authnRequest, RequestedAttribute reqAttr) {
		if (authnRequest != null) {
			RequestedAttributes requestedAttributes = (RequestedAttributes) authnRequest.getExtensions().getUnknownXMLObjects(RequestedAttributes.DEFAULT_ELEMENT_NAME).get(0);
			requestedAttributes.getRequestedAttributes().add(reqAttr);
		}
	}
	
	/**
	 * Adds several RequestedAttribute to STORKAuthnRequest
	 * @param authnRequest STORKAuthnRequest
	 * @param reqAttr RequestedAttribute
	 */
	public static void addRequestedAttributes(STORKAuthnRequest authnRequest, RequestedAttribute... reqAttrs) {
		for (RequestedAttribute reqAttr : reqAttrs) {
			addRequestedAttribute(authnRequest, reqAttr);
		}
	}
	
	/**
	 * Builds STORK signed doc attribute and adds it to STORKAuthnRequest
	 * @param authnRequest STORKAuthnRequest
	 * @param textToBeSigned Text to be included in the DSS request
	 * @param mimeType MimeType of the contents
	 * @param isRequired true or false if signedDoc RequestedAttribute is required
	 */
	public static void buildAndAddSignatureRequestToAuthnRequest(STORKAuthnRequest authnRequest, String textToBeSigned, String mimeType, boolean isRequired) {
		if (authnRequest != null && !StringUtils.isEmpty(textToBeSigned)) {
			addRequestedAttribute(authnRequest, buildSignatureRequestRequestedAttribute(textToBeSigned, mimeType, isRequired));
		}
		
	}
	
	/**
	 * Adds DSS siganture request as String to STORKAuthnRequest
	 * @param authnRequest STORKAuthnRequest
	 * @param dssSignatureRequest DSS signature request as String
	 * @param isRequired true or false if signedDoc RequestedAttribute is required
	 */
	public static void addSignatureRequestToAuthnRequest(STORKAuthnRequest authnRequest, String dssSignatureRequest, boolean isRequired) {
		if (authnRequest != null && !StringUtils.isEmpty(dssSignatureRequest)) {
			addRequestedAttribute(authnRequest, buildRequestedAttribute(STORKConstants.STORK_ATTRIBUTE_SIGNEDDOC, isRequired, dssSignatureRequest));
		}
		
	}
	
	/**
	 * Adds Attribute to an assertion
	 * @param assertion Assertion
	 * @param attr Attribute
	 */
	public static void addAttribute(Assertion assertion, Attribute attr) {
		if (assertion != null) {
			if (!assertion.getAttributeStatements().isEmpty()) {
				assertion.getAttributeStatements().get(0).getAttributes().add(attr);
			}
		}
	}
	
	/**
	 * Adds several Attribute to an assertion
	 * @param assertion Assertion
	 * @param attr Attribute
	 */
	public static void addAttributes(Assertion assertion, Attribute... attrs) {
		for (Attribute attr : attrs) {
			addAttribute(assertion, attr);
		}
	}
	
	/**
	 * Adds several Attribute to first assertion in STORK response
	 * @param response STORK response
	 * @param attrs Attribute
	 */
	public static void addAttribute(STORKResponse response, Attribute... attrs) {
		if (response != null) {
			if (!response.getAssertions().isEmpty()) {
				addAttributes(response.getAssertions().get(0), attrs);
			}
		}
	}

}
