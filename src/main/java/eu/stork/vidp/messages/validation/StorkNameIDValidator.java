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

import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.validator.NameIDSchemaValidator;
import org.opensaml.xml.validation.ValidationException;

public class StorkNameIDValidator extends NameIDSchemaValidator {

	private static final String FORMAT_ALLOWED_VALUE = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";
	private static final String FORMAT_ALLOWED_VALUE_OLD = "urn:oasis:names:tc:SAML:2.0:nameid-format:unspecified";
	
	/**
	 * Constructor
	 * 
	 */
	public StorkNameIDValidator() {

		super();
	}

	@Override
	public void validate(NameID nameID) throws ValidationException {

		super.validate(nameID);

		if (nameID.getNameQualifier() == null) {

			throw new ValidationException("NameQualifier is required.");
		}

		if (nameID.getFormat() == null) {
			
			throw new ValidationException("Format is required.");
			
		} else if(!(nameID.getFormat().equals(FORMAT_ALLOWED_VALUE) || nameID.getFormat().equals(FORMAT_ALLOWED_VALUE_OLD))) {

			throw new ValidationException("Format is invalid.");
		}

	}

}
