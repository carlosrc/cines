package es.udc.pa.pa006.cines.web.pages.user;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;

import es.udc.pa.pa006.cines.model.userprofile.UserProfile;
import es.udc.pa.pa006.cines.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa006.cines.model.userservice.UserService;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.CookiesManager;
import es.udc.pa.pa006.cines.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

    @Property
    private String loginName;

    @Property
    private String password;

    @Property
    private boolean rememberMyPassword;

    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private Cookies cookies;

    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Inject
    private UserService userService;

    private UserProfile userProfile = null;

    @Inject
    private Request request;

    void onValidateFromLoginForm() {

        if (!loginForm.isValid()) {
            return;
        }

        try {
            userProfile = userService.login(loginName, password, false);
        } catch (InstanceNotFoundException e) {
            loginForm.recordError(messages.get("error-authenticationFailed"));
        } catch (IncorrectPasswordException e) {
            loginForm.recordError(messages.get("error-authenticationFailed"));
        }

    }

    Object onSuccess() throws MalformedURLException {

    	userSession = new UserSession();
        userSession.setUserProfileId(userProfile.getUserProfileId());
        userSession.setFirstName(userProfile.getFirstName());
        userSession.setRole(userProfile.getRole());

        if (rememberMyPassword) {
            CookiesManager.leaveCookies(cookies, loginName, userProfile
                    .getEncryptedPassword());
        }
        return new URL(request.getHeader("referer"));

    }

}
