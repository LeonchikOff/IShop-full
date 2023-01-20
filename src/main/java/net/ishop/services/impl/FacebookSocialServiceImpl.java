package net.ishop.services.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;

import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.dependency_injection.Value;
import net.ishop.models.social.SocialAccount;
import net.ishop.services.ServiceManager;
import net.ishop.services.interfaces.SocialService;

@Component
public class FacebookSocialServiceImpl implements SocialService {
    @Value(value = "social.facebook.idClient")
    private String idClient;
    @Value(value = "social.facebook.secret")
    private String secret;
    @Value(value = "app.host")
    private String host;

    public FacebookSocialServiceImpl() {
    }


    private String getRedirectUrl() {
        return host + "/from-social";
    }

    @Override
    public String getAuthorizeUrl() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(FacebookPermissions.EMAIL);
        FacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_4_0);
        return facebookClient.getLoginDialogUrl(idClient, getRedirectUrl(), scopeBuilder);
    }

    @Override
    public SocialAccount getSocialAccount(String authToken) {
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_4_0);
        FacebookClient.AccessToken userAccessToken = facebookClient.obtainUserAccessToken(idClient, secret, getRedirectUrl(), authToken);
        facebookClient = new DefaultFacebookClient(userAccessToken.getAccessToken(), Version.VERSION_4_0);
        User user = facebookClient.fetchObject("me", User.class,
                Parameter.with("fields", "name,email,first_name,last_name"));
        String avatarUrl = String.format("https://graph.facebook.com/v15.0/%s/picture?access_token=%s", user.getId(), userAccessToken.getAccessToken());
        return new SocialAccount(user.getFirstName(), user.getEmail(), avatarUrl);
    }
}
