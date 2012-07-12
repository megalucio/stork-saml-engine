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

import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.validator.ConditionsSpecValidator;
import org.opensaml.xml.validation.ValidationException;

public class StorkConditionsValidator extends ConditionsSpecValidator {

	/**
	 * Constructor
	 * 
	 */
	public StorkConditionsValidator() {
		
		super();
	}
	
	@Override
    public void validate(Conditions conditions) throws ValidationException {

		super.validate(conditions);
		
		if(conditions.getNotBefore() == null) {
			
			throw new ValidationException("NotBefore is required.");
		}
		
		if(conditions.getNotOnOrAfter() == null) {
			
			throw new ValidationException("NotOnOrAfter is required.");
		}
		
		if(conditions.getAudienceRestrictions() == null || conditions.getAudienceRestrictions().size() < 1) {
			
			throw new ValidationException("AudienceRestriction is required.");
		}
		
		if(conditions.getOneTimeUse() == null) {
			
			throw new ValidationException("OneTimeUse is required.");
		}
		
	}
	
}
