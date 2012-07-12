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


package eu.stork.vidp.messages.saml.impl;

import java.security.cert.X509Certificate;
import java.util.List;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.impl.AuthnRequestImpl;
import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.xml.XMLObject;

import eu.stork.mw.messages.saml.STORKAuthnRequest;

public class STORKAuthnRequestImpl extends AuthnRequestImpl implements STORKAuthnRequest {
	
	private int qaaLevel;
	
	private String ccc;
	
	private String finalRedirectURL;
	
	private String spID;
	
	private XMLObject originalSPAuthRequest;
	
	private X509Certificate spCertSig;
	
	private X509Certificate spCertEnc;
	
	//private XMLObjectChildrenList<RequestedAttribute> requestedAttributes;
	private List<RequestedAttribute> requestedAttributes;
 
	protected STORKAuthnRequestImpl(String namespaceURI, String elementLocalName,
			String namespacePrefix) {
		super(namespaceURI, elementLocalName, namespacePrefix);		
		//requestedAttributes = new IndexedXMLObjectChildrenList<RequestedAttribute>(this);
	}	
		
	public STORKAuthnRequestImpl() {
		super(SAMLConstants.SAML20P_NS, STORKAuthnRequest.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
	}



	public int getQAALevel() {
		return this.qaaLevel;
	}

	public void setQAALevel(int authLevel) {
		this.qaaLevel = authLevel;
		
	}

	public String getCitizenCountryCode() {		
		return ccc;
	}

	public String getFinalRedirectURL() {
		return finalRedirectURL;
	}

	public XMLObject getOriginalSPAuthRequest() {
		return originalSPAuthRequest;
	}

	public X509Certificate getSPCertEnc() {
		return spCertEnc;
	}

	public X509Certificate getSPCertSig() {
		return spCertSig;
	}

	public String getSPID() {
		return spID;
	}

	public void setCitizenCountryCode(String citizenCountryCode) {
		this.ccc = citizenCountryCode;
	}

	public void setFinalRedirectURL(String finalRedirectURL) {
		this.finalRedirectURL = finalRedirectURL;
	}

	public void setOriginalSPAuthRequest(XMLObject spAuthRequest) {
		this.originalSPAuthRequest = spAuthRequest;
	}

	public void setSPCertEnc(X509Certificate encryptionCertificate) {
		this.spCertEnc = encryptionCertificate;
	}

	public void setSPCertSig(X509Certificate signingCertificate) {
		this.spCertSig = signingCertificate;
	}

	public void setSPID(String spID) {
		this.spID = spID;
	}

	public List<RequestedAttribute> getRequestedAttributes() {		
//		return (List<RequestedAttribute>) requestedAttributes.subList(new QName(STORKMessagesConstants.STORK10_NS, DEFAULT_ELEMENT_LOCAL_NAME, STORKMessagesConstants.STORK10_PREFIX));
		return requestedAttributes;
	}

	public void setRequestedAttributes(List<RequestedAttribute> requestedAttributesList) {
		// this.requestedAttributes = (XMLObjectChildrenList<RequestedAttribute>) requestedAttributesList;
		this.requestedAttributes = requestedAttributesList;
	}
	
//	public List<XMLObject> getOrderedChildren() {
//        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
//
//        if (super.getOrderedChildren() != null) {
//            children.addAll(super.getOrderedChildren());
//        }
//
//        if (qaaLevel != 0 ) {        	
//            children.add(subject);
//        }
//
//        if (nameIDPolicy != null) {
//            children.add(nameIDPolicy);
//        }
//
//        if (conditions != null) {
//            children.add(conditions);
//        }
//
//        if (requestedAuthnContext != null) {
//            children.add(requestedAuthnContext);
//        }
//
//        if (scoping != null) {
//            children.add(scoping);
//        }
//
//        if (children.size() == 0) {
//            return null;
//        }
//
//        return Collections.unmodifiableList(children);
//    }

}
