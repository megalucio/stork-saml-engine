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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.opensaml.saml2.core.impl.AuthnRequestUnmarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.util.Base64;

import eu.stork.mw.messages.saml.STORKAuthnRequest;
import eu.stork.vidp.messages.saml.STORKExtensions;
import eu.stork.vidp.messages.stork.SPCertEnc;
import eu.stork.vidp.messages.stork.SPCertSig;
import eu.stork.vidp.messages.stork.SPCertType;
import eu.stork.vidp.messages.stork.SPInformation;
import eu.stork.vidp.messages.stork.VIDPAuthenticationAttributes;

public class STORKAuthnRequestUnmarshaller extends AuthnRequestUnmarshaller {
	
	protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
    throws UnmarshallingException {
		STORKAuthnRequest req = (STORKAuthnRequest) parentSAMLObject;
		
		if (childSAMLObject instanceof STORKExtensions) {
			STORKExtensions ext = (STORKExtensions) childSAMLObject;
			req.setExtensions(ext);
			
			if (ext.getQAALevel() != null)
				req.setQAALevel(ext.getQAALevel().getValue());
			
			if (ext.getRequestedAttributes() != null) {
				//List<RequestedAttribute> reqAttrList = new ArrayList<RequestedAttribute>();
//				for (RequestedAttribute reqAtt : ext.getRequestedAttributes().getRequestedAttributes()) {
//					req.getRequestedAttributes().add(reqAtt);
//				}
				req.setRequestedAttributes(ext.getRequestedAttributes().getRequestedAttributes());
									
			}
			
			if (ext.getAuthenticationAttributes() != null) {
				VIDPAuthenticationAttributes vidpAuthAttributes = ext.getAuthenticationAttributes().getVIDPAuthenticationAttributes();
				if (vidpAuthAttributes != null) {
					if (vidpAuthAttributes.getCitizenCountryCode() != null)
						req.setCitizenCountryCode(vidpAuthAttributes.getCitizenCountryCode().getValue());
															
					SPInformation spInformation = vidpAuthAttributes.getSPInformation();
					if (spInformation != null) {
						if (spInformation.getSPID() != null)
							req.setSPID(spInformation.getSPID().getValue());
						
						if (spInformation.getSPCertSig() != null) {
							SPCertSig spCertSig = spInformation.getSPCertSig();
							try {
								req.setSPCertSig(getCertificateFromX509Data(spCertSig));
							} catch (Exception e) {
								throw new UnmarshallingException("Error reading SP signing certificate");
							}
						}
						
						if (spInformation.getSPCertEnc() != null) {
							SPCertEnc spCertEnc = spInformation.getSPCertEnc();
							try {
								req.setSPCertEnc(getCertificateFromX509Data(spCertEnc));
							} catch (Exception e) {
								throw new UnmarshallingException("Error reading SP encryption certificate");
							}
						}
						
						if (spInformation.getSPAuthRequest() != null) {
							req.setOriginalSPAuthRequest(spInformation.getSPAuthRequest());
						}
						
					}
				}
			}
			
		} else {
		    super.processChildElement(parentSAMLObject, childSAMLObject);
		}
	}
	
	private X509Certificate getCertificateFromX509Data(SPCertType spCert) throws CertificateException {
		if (spCert.getKeyInfo() != null)
			if (!spCert.getKeyInfo().getX509Datas().isEmpty()) {
				X509Data samlX509Data = spCert.getKeyInfo().getX509Datas().get(0);
				
				if (samlX509Data != null) {
					if (!samlX509Data.getX509Certificates().isEmpty()) {
						org.opensaml.xml.signature.X509Certificate samlX509Cert = samlX509Data.getX509Certificates().get(0);
						if (samlX509Cert != null) {
							if (samlX509Cert.getValue() != null && samlX509Cert.getValue().length() != 0) {
								InputStream inStream = new ByteArrayInputStream( Base64.decode(samlX509Cert.getValue()));
								CertificateFactory cf = CertificateFactory.getInstance("X.509");
								X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
								return cert;
							}

						}
						
					}
				}
			}
		
		return null;
	}

}
