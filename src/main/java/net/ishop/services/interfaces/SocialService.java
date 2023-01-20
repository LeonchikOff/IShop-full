package net.ishop.services.interfaces;

import net.ishop.models.social.SocialAccount;

public interface SocialService {

    String getAuthorizeUrl();
    SocialAccount getSocialAccount(String authToken);
}
