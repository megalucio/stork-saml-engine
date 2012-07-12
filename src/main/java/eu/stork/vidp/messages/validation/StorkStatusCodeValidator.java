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

import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.validator.StatusCodeSchemaValidator;
import org.opensaml.xml.validation.ValidationException;

public class StorkStatusCodeValidator extends StatusCodeSchemaValidator {

	// supported values according to SAML v2.0 specification
	private static String[] ALLOWED_FIRST_LEVEL_STATUS_CODE_VALUES = new String[] {
		"urn:oasis:names:tc:SAML:2.0:status:Success",
		"urn:oasis:names:tc:SAML:2.0:status:Requester",
		"urn:oasis:names:tc:SAML:2.0:status:Responder",		
		"urn:oasis:names:tc:SAML:2.0:status:VersionMismatch"};
	
	private static String[] ALLOWED_SECOND_LEVEL_STATUS_CODE_VALUES = new String[] {
		"urn:oasis:names:tc:SAML:2.0:status:AuthnFailed",
		"urn:oasis:names:tc:SAML:2.0:status:InvalidAttrNameOrValue",
		"urn:oasis:names:tc:SAML:2.0:status:InvalidNameIDPolicy",
		"urn:oasis:names:tc:SAML:2.0:status:NoAuthnContext",
		"urn:oasis:names:tc:SAML:2.0:status:NoAvailableIDP",
		"urn:oasis:names:tc:SAML:2.0:status:NoPassive",
		"urn:oasis:names:tc:SAML:2.0:status:NoSupportedIDP",
		"urn:oasis:names:tc:SAML:2.0:status:PartialLogout",
		"urn:oasis:names:tc:SAML:2.0:status:ProxyCountExceeded",
		"urn:oasis:names:tc:SAML:2.0:status:RequestDenied",
		"urn:oasis:names:tc:SAML:2.0:status:RequestUnsupported",
		"urn:oasis:names:tc:SAML:2.0:status:RequestVersionDeprecated",
		"urn:oasis:names:tc:SAML:2.0:status:RequestVersionTooHigh",
		"urn:oasis:names:tc:SAML:2.0:status:RequestVersionTooLow",
		"urn:oasis:names:tc:SAML:2.0:status:ResourceNotRecognized",
		"urn:oasis:names:tc:SAML:2.0:status:TooManyResponses",
		"urn:oasis:names:tc:SAML:2.0:status:UnknownAttrProfile",
		"urn:oasis:names:tc:SAML:2.0:status:UnknownPrincipal",
		"urn:oasis:names:tc:SAML:2.0:status:UnsupportedBinding",
		"http://www.stork.gov.eu/saml20/statusCodes/QAANotSupported"
	};
	
	/**
	 * Constructor
	 * 
	 */
	public StorkStatusCodeValidator() {

		super();
	}

	@Override
	public void validate(StatusCode statusCode) throws ValidationException {

		super.validate(statusCode);
		
		
		if(statusCode.getValue() == null) {
			
			throw new ValidationException("StatusCode is required");
		} 
		
		boolean valid = false;
			
		if (statusCode.getParent() instanceof Status) {
			//first level Status Codes
		
			String value = statusCode.getValue();
			
			
			
			
			for(String allowedVal : ALLOWED_FIRST_LEVEL_STATUS_CODE_VALUES) {
				
				if(value.equals(allowedVal)) {
					
					valid = true;
					break;
				}
			}
			
			if(!valid) {
				
				throw new ValidationException("First Level StatusCode has an invalid value.");
			}
		} else {
			//parent is status code
			//second level Status Codes						
			
			if(statusCode != null) {
				
				valid = false;
				
				String subVal = statusCode.getValue();
				
				for(String allowedVal : ALLOWED_SECOND_LEVEL_STATUS_CODE_VALUES) {
					
					if(subVal.equals(allowedVal)) {
						
						valid = true;
						break;
					}
				}
				
				if(!valid) {
					
					throw new ValidationException("Second Level StatusCode has an invalid value.");
				}
				
			}
			
		}
		}
		
	
	
}
