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

import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

import eu.stork.vidp.messages.stork.SpInstitution;

public class StorkSpInstitutionValidator implements
		Validator<SpInstitution> {
	
	public static final int MIN_SIZE = 1;
	public static final int MAX_SIZE = 50;
	//public static final String REGEX_PATTERN = "^[a-zA-Z0-9]{1,50}$";

	public StorkSpInstitutionValidator() {
		
	}

	public void validate(SpInstitution spInstitution) throws ValidationException {
		
		if(spInstitution != null) {
			
			if (spInstitution.getValue() == null) {
				throw new ValidationException("spInstitution has no value");
			}
									
//			if (!Pattern.matches(REGEX_PATTERN, spApplication.getValue())) {
//				throw new ValidationException("spApplication has wrong format: " + spApplication.getValue());
//			}
						
			if (spInstitution.getValue().length() < MIN_SIZE || spInstitution.getValue().length() > MAX_SIZE) {
				throw new ValidationException("spInstitution has wrong size: " + spInstitution.getValue().length());
			}
		}
	}
	
}
