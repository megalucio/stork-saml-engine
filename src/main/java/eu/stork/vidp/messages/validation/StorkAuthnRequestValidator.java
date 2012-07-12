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


package eu.stork.vidp.messages.validation;

import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.validator.AuthnRequestSchemaValidator;
import org.opensaml.xml.util.XMLHelper;
import org.opensaml.xml.validation.ValidationException;

import eu.stork.mw.messages.saml.STORKAuthnRequest;

public class StorkAuthnRequestValidator extends AuthnRequestSchemaValidator {

	private static final String ALLOWED_CONSENT = "urn:oasis:names:tc:SAML:2.0:consent:unspecified";
	private static final String ALLOWED_PROTOCOL_BINDING_1 = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST";
	private static final String ALLOWED_PROTOCOL_BINDING_2 = "urn:oasis:names:tc:SAML:2.0:bindings:SOAP";
	
	private static final int MAX_SIZE = 131072;

	/**
	 * Constructor
	 * 
	 */
	public StorkAuthnRequestValidator() {

		super();
	}

	@Override
	public void validate(AuthnRequest req) throws ValidationException {
				
		if (XMLHelper.prettyPrintXML(req.getDOM()).getBytes().length > MAX_SIZE) {
			throw new ValidationException("SAML AuthnRequest exceeds max size.");
		}
		
		super.validate(req);

		STORKAuthnRequest request = (STORKAuthnRequest) req;

		if (request.getID() == null) {

			throw new ValidationException("ID is required.");
		}

		if (request.getVersion() == null) {

			throw new ValidationException("Version is required.");
		} else {

			if (!request.getVersion().equals(SAMLVersion.VERSION_20)) {

				throw new ValidationException("Version is invalid.");
			}
		}

		if (request.getIssueInstant() == null) {

			throw new ValidationException("IssueInstant is required.");
		}

		if (request.getConsent() != null) {

			if (!request.getConsent().equals(ALLOWED_CONSENT)) {

				throw new ValidationException("Consent is invalid.");
			}
		}

		if (request.isForceAuthn() == null) {

			throw new ValidationException("ForceAuthn is required.");
		} else if (!request.isForceAuthn()) {

			throw new ValidationException("ForceAuthn is invalid.");
		}

		if (request.isPassive() == null) {

			throw new ValidationException("IsPassive is required.");
		} else if (request.isPassive()) {

			throw new ValidationException("IsPassive is invalid.");
		}

		if (request.getProtocolBinding() == null) {

			throw new ValidationException("ProtocolBinding is required.");
		} else {
			if (!request.getProtocolBinding()
					.equals(ALLOWED_PROTOCOL_BINDING_1)
					&& !request.getProtocolBinding().equals(
							ALLOWED_PROTOCOL_BINDING_2)) {

				throw new ValidationException("ProtocolBinding is invalid.");
			}

		}

		if(request.getAssertionConsumerServiceURL() == null) {
			
			throw new ValidationException("AssertionConsumerServiceURL is required.");
		}

		if(request.getProviderName() == null) {
			
			throw new ValidationException("ProviderName is required.");
		}
		
		
		
	}

}
