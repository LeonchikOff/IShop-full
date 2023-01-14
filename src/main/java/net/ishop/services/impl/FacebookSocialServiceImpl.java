package net.ishop.services.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;

import net.ishop.models.social.SocialAccount;
import net.ishop.services.SocialService;

public class FacebookSocialServiceImpl implements SocialService {
    public final String idClient;
    public final String secret;
    public final String redirectUrl;

    public FacebookSocialServiceImpl(ServiceManager serviceManager) {
        idClient = serviceManager.getApplicationProperty("social.facebook.idClient");
        secret = serviceManager.getApplicationProperty("social.facebook.secret");
        redirectUrl = serviceManager.getApplicationProperty("app.host") + "/from-social";
    }

    @Override
    public String getAuthorizeUrl() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(FacebookPermissions.EMAIL);
        FacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_4_0);
        return facebookClient.getLoginDialogUrl(idClient, redirectUrl, scopeBuilder);
    }

    @Override
    public SocialAccount getSocialAccount(String authToken) {
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(Version.VERSION_4_0);
        FacebookClient.AccessToken userAccessToken = facebookClient.obtainUserAccessToken(idClient, secret, redirectUrl, authToken);
        facebookClient = new DefaultFacebookClient(userAccessToken.getAccessToken(), Version.VERSION_4_0);
        User user = facebookClient.fetchObject("me", User.class,
                Parameter.with("fields", "name,email,first_name,last_name"));
        String avatarUrl = String.format("https://graph.facebook.com/v15.0/%s/picture?access_token=%s", user.getId(), userAccessToken.getAccessToken());
        return new SocialAccount(user.getFirstName(), user.getEmail(), avatarUrl);
    }
}
