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


package eu.stork.mw.messages.saml;

import java.security.cert.X509Certificate;
import java.util.List;

import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.metadata.RequestedAttribute;
import org.opensaml.xml.XMLObject;

/**
 * Interface extending a SAML AuthnRequest by additional attributes required by STORK
 * @author bzwattendorfer
 *
 */
public interface STORKAuthnRequest extends AuthnRequest {
	
	/**
	 * Sets the ID of the requesting Service Provider
	 * @param spID ID of the Service Provider
	 */
	public void setSPID(String spID);
	
	/**
	 * Gets the ID of the Service Provider
	 * @return ID of the Service Provider
	 */
	public String getSPID();
	
	/**
	 * Sets the citizen country code
	 * @param citizenCountryCode citizen country code
	 */
	public void setCitizenCountryCode(String citizenCountryCode);
	
	/**
	 * Gets the citizen country code
	 * @return citizen country code
	 */
	public String getCitizenCountryCode();
	
	/**
	 * Sets the final redirect URL
	 * @param finalRedirectURL Final redirect URL
	 */
	public void setFinalRedirectURL(String finalRedirectURL);
	
	/**
	 * Gets the final redirect URL
	 * @return final redirect URL
	 */
	public String getFinalRedirectURL();
	
	/**
	 * Sets the signing certificate of the service provider
	 * @param signingCertificate Signing certificate of the SP
	 */
	public void setSPCertSig(X509Certificate signingCertificate);
	
	/**
	 * Gets the signing certificate of the service provider
	 * @return signing certificate of the service provider
	 */
	public X509Certificate getSPCertSig();
	
	/**
	 * Sets the encryption certificate of the service provider
	 * @param encryptionCertificate encryption certificate of the SP
	 */
	public void setSPCertEnc(X509Certificate encryptionCertificate);
	
	/**
	 * Gets the encryption certificate of the service provider
	 * @return encryption certificate of the SP
	 */
	public X509Certificate getSPCertEnc();
	
	
	/**
	 * Sets the original authentication request of the service provider
	 * @param spAuthRequest original SP authentication request
	 */
	public void setOriginalSPAuthRequest(XMLObject spAuthRequest);
	
	/**
	 * Gets the original authentication request of the service provider
	 * @return original SP authentication request
	 */
	public XMLObject getOriginalSPAuthRequest();
	
	/**
	 * Sets the requested STORK QAA level
	 * @param authLevel Requested STORK QAA level
	 */
	public void setQAALevel(int authLevel);
	
	/**
	 * Gets the requested STORK QAA level
	 * @return Requested STORK QAA level
	 */
	public int getQAALevel();
	
	/**
	 * Gets a list of requested attributes
	 * @return List containg all requested attributes
	 */
	public List<RequestedAttribute> getRequestedAttributes();
	
	/**
	 * Sets the requested attributes
	 * @param requestedAttributesList List containg all requested attributes
	 */
	public void setRequestedAttributes(List<RequestedAttribute> requestedAttributesList);

}
