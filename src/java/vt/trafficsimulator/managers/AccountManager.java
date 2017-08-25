/*
 * Created by Sait Tuna Onder on 2017.08.25  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.trafficsimulator.managers;

import javax.enterprise.context.Dependent;
import vt.trafficsimulator.entityclasses.User;
import vt.trafficsimulator.entityclasses.MapImage;

import vt.trafficsimulator.sessionbeans.UserFacade;
import vt.trafficsimulator.sessionbeans.MapImageFacade;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

/**
 *
 * @author Onder
 */
@Named(value = "accountManager")
@SessionScoped

public class AccountManager implements Serializable {
    
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    
    private String statusMessage;

    private User selected;
    
    @EJB
    private UserFacade userFacade;
    
    public AccountManager() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    protected UserFacade getUserFacade() {
        return userFacade;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    
    
     /*
    ================
    Instance Methods
    ================
     */
    // Return True if a user is logged in; otherwise, return False
    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("email") != null;
    }

    /*
    Create a new new user account. Return "" if an error occurs; otherwise,
    upon successful account creation, redirect to show the SignIn page.
     */
    public String createAccount() {
        
        // First, check if the entered email is already being used

        // Obtain the object reference of a User object with email
        User aUser = getUserFacade().findByEmail(email);

        if (aUser != null) {
            // A user already exists with the email entered
            email = "";
            statusMessage = "Email already exists!";
            return "";
        }
        
        // The entered email is available

        if (statusMessage == null || statusMessage.isEmpty()) {
            try {
                // Instantiate a new User object
                User newUser = new User();

                /*
                Set the properties of the newly created newUser object with the values
                entered by the user in the AccountCreationForm in CreateAccount.xhtml
                */
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setEmail(email);
                newUser.setPassword(password);
                
                getUserFacade().create(newUser);

            } catch (EJBException e) {
                email = "";
                statusMessage = "Something went wrong while creating user's account! See: " + e.getMessage();
                return "";
            }
            // Initialize the session map for the newly created User object
            initializeSessionMap();

            /*
            The Profile page cannot be shown since the new User has not signed in yet.
            Therefore, we show the Sign In page for the new User to sign in first.
             */
            return "SignIn.xhtml?faces-redirect=true";
        }
        return "";
    }
    
      // Validate if the entered password and confirm password are correct
    public void validateUserPassword(ComponentSystemEvent event) {
        /*
        FacesContext contains all of the per-request state information related to the processing of
        a single JavaServer Faces request, and the rendering of the corresponding response.
        It is passed to, and potentially modified by, each phase of the request processing lifecycle.
         */
        FacesContext fc = FacesContext.getCurrentInstance();

        /*
        UIComponent is the base class for all user interface components in JavaServer Faces. 
        The set of UIComponent instances associated with a particular request and response are organized into
        a component tree under a UIViewRoot that represents the entire content of the request or response.
         */
        // Obtain the UIComponent instances associated with the event
        UIComponent components = event.getComponent();

        /*
        UIInput is a kind of UIComponent for the user to enter a value in.
         */
        // Obtain the object reference of the UIInput field with id="password" on the UI
        UIInput uiInputPassword = (UIInput) components.findComponent("password");

        // Obtain the password entered in the UIInput field with id="password" on the UI
        String entered_password = uiInputPassword.getLocalValue()
                == null ? "" : uiInputPassword.getLocalValue().toString();

        // Obtain the object reference of the UIInput field with id="confirmPassword" on the UI
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmPassword");

        // Obtain the confirm password entered in the UIInput field with id="confirmPassword" on the UI
        String entered_confirm_password = uiInputConfirmPassword.getLocalValue()
                == null ? "" : uiInputConfirmPassword.getLocalValue().toString();

        if (entered_password.isEmpty() || entered_confirm_password.isEmpty()) {
            // Do not take any action. 
            // The required="true" in the XHTML file will catch this and produce an error message.
            return;
        }

        if (!entered_password.equals(entered_confirm_password)) {
            statusMessage = "Password and Confirm Password must match!";
        } else {
            // Obtain the logged-in User's username
            String user_email = (String) FacesContext.getCurrentInstance().
                    getExternalContext().getSessionMap().get("email");

            // Obtain the object reference of the signed-in User object
            User user = getUserFacade().findByEmail(user_email);

            if (entered_password.equals(user.getPassword())) {
                // entered password = signed-in user's password
                statusMessage = "";
            } else {
                statusMessage = "Incorrect Password!";
            }
        }
    }

    // Initialize the session map for the User object
    public void initializeSessionMap() {

        // Obtain the object reference of the User object
        User user = getUserFacade().findByEmail(getEmail());

        // Put the User's object reference into session map variable user
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user", user);

        // Put the User's database primary key into session map variable user_id
        FacesContext.getCurrentInstance().getExternalContext().
                getSessionMap().put("user_id", user.getId());
    }

    /*
    UIComponent is the base class for all user interface components in JavaServer Faces. 
    The set of UIComponent instances associated with a particular request and response are organized into
    a component tree under a UIViewRoot that represents the entire content of the request or response.
     
    @param components: UIComponent instances associated with the current request and response
    @return True if entered password is correct; otherwise, return False
     */
    private boolean correctPasswordEntered(UIComponent components) {

        // Obtain the object reference of the UIInput field with id="verifyPassword" on the UI
        UIInput uiInputVerifyPassword = (UIInput) components.findComponent("verifyPassword");

        // Obtain the verify password entered in the UIInput field with id="verifyPassword" on the UI
        String verifyPassword = uiInputVerifyPassword.getLocalValue()
                == null ? "" : uiInputVerifyPassword.getLocalValue().toString();

        if (verifyPassword.isEmpty()) {
            statusMessage = "Please enter a password!";
            return false;

        } else if (verifyPassword.equals(password)) {
            // Correct password is entered
            return true;

        } else {
            statusMessage = "Invalid password entered!";
            return false;
        }
    }

    // Show the Home page
    public String showHomePage() {
        return "index?faces-redirect=true";
    }
   
    public String logout() {
        // Clear the logged-in User's session map
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();

        // Reset the logged-in User's properties
        email = password = "";
        firstName = lastName = "";
        statusMessage = "";

        // Invalidate the logged-in User's session
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        // Redirect to show the index (Home) page
        return "index.xhtml?faces-redirect=true";
    }
    
    
}
