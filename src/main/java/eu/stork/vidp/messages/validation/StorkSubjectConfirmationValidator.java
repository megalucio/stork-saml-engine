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

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.validator.SubjectConfirmationSchemaValidator;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.signature.X509Data;
import org.opensaml.xml.validation.ValidationException;

public class StorkSubjectConfirmationValidator extends
		SubjectConfirmationSchemaValidator {

	private static final String ALLOWED_METHOD_1 = "urn:oasis:names:tc:SAML:2.0:cm:bearer";
	private static final String ALLOWED_METHOD_2 = "oasis:names:tc:SAML:2.0:cm:holder-of-key";

	/**
	 * Constructor
	 * 
	 */
	public StorkSubjectConfirmationValidator() {

		super();
	}

	@Override
	public void validate(SubjectConfirmation subjectConfirmation)
			throws ValidationException {

		super.validate(subjectConfirmation);
		
		String method = subjectConfirmation.getMethod();

		if (!(method.equals(ALLOWED_METHOD_1) || method.equals(ALLOWED_METHOD_2))) {
			throw new ValidationException("Method is invalid.");	
		} 
		
		if (subjectConfirmation.getSubjectConfirmationData() == null) {
			throw new ValidationException("SubjectConfirmationData required.");
			
		} 

		SubjectConfirmationData confData = subjectConfirmation.getSubjectConfirmationData();
		
		
		if (method.equals(ALLOWED_METHOD_1)) {
			if (confData.getNotBefore() != null) {
				throw new ValidationException("NotBefore in SubjectConfirmationData not allowed if confirmation method is \"bearer\".");
			}

		}

		if (confData.getNotOnOrAfter() == null) {

			throw new ValidationException("NotOnOrAfter is required.");
		}

		if (confData.getRecipient() == null) {

			throw new ValidationException("Recipient is required.");
		}

		if (confData.getInResponseTo() == null) {

			throw new ValidationException("InResponseTo is required.");
		}
				
		if(method.equals(ALLOWED_METHOD_2)) {
					
			List<XMLObject> childrenKeyInfo =  confData.getUnknownXMLObjects(new QName("KeyInfo"));
						
			if(childrenKeyInfo.size() < 1) {
						
				throw new ValidationException("KeyInfo is required.");
			}
					
			List<XMLObject> childrenKeyData = confData.getUnknownXMLObjects(new QName("X509Data"));
					
			if(childrenKeyData.size() != 1) {
						
				throw new ValidationException("Invalid number of X509Data elements.");
			} else {
						
				X509Data data = (X509Data)childrenKeyData.get(0);
						
				if(data.getX509Certificates() == null || data.getX509Certificates().size() < 1 ) {
							
					throw new ValidationException("X509Certificate is required.");
				}
						
			}
				
		}
			
		

	}

	
}
