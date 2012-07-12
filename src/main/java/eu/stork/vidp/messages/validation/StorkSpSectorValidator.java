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

import java.util.regex.Pattern;

import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;

import eu.stork.vidp.messages.stork.SpSector;

public class StorkSpSectorValidator implements
		Validator<SpSector> {
	
	public static final int MIN_SIZE = 1;
	public static final int MAX_SIZE = 20;
	//public static final String REGEX_PATTERN = "^[a-zA-Z0-9]{1,30}$";

	public StorkSpSectorValidator() {
		
	}

	public void validate(SpSector spSector) throws ValidationException {
		
		if(spSector != null) {
			
			if (spSector.getValue() == null) {
				throw new ValidationException("spSector has no value");
			}
			
//			if (!Pattern.matches(REGEX_PATTERN, spSector.getValue())) {
//				throw new ValidationException("spSector has wrong format: " + spSector.getValue());
//			}
			
			if (spSector.getValue().length() < MIN_SIZE || spSector.getValue().length() > MAX_SIZE) {
				throw new ValidationException("spApplication has wrong size: " + spSector.getValue().length());
			}
			
		}
	}
	
}
