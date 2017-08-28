/*
 * Created by Sait Tuna Onder on 2017.08.25  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficsimulator.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        // Type cast the inputted "value" to email of type String
        String email = (String) value;

        if (email == null || email.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }
        
        /*
            ^			# start of the regex line
            [_A-Za-z0-9-\\+]+   # can start with underscore, A to Z, a to z, 0 to 9, hyphen, and can contain one or more "+"
            (			# start of group 1
            \\.[_A-Za-z0-9-]+	# after a dot "." it can start with underscore, A to Z, a to z, 0 to 9, or hyphen
            )*			# end of group1. "*" designates this group as optional.
            @			# must contain the "@" symbol
            [A-Za-z0-9-]+       # after the "@" symbol, it can start with A to Z, a to z, 0 to 9, or hyphen
            (			# start of group 2
            \\.[A-Za-z0-9]+     # after a dot "." it can start with A to Z, a to z, or 0 to 9
            )*                  # end of group 2. "*" designates this group as optional.
            (			# start of group 3
            \\.[A-Za-z]{2,}     # after a dot "." it can start with A to Z or a to z, with minimum length of 2
            )			# end of group #3
            $			# end of the regex line
        */
        
        // REGular EXpression (regex) to validate the email address entered
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        
        if (!email.matches(regex)) {
            throw new ValidatorException(new FacesMessage("Please Enter a Valid Email Address!"));
        }        
    } 
    
}
