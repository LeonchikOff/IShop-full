package net.ishop.entities;

import net.ishop.models.social.CurrentAccount;

public class Account extends AbstractEntity<Integer> implements CurrentAccount {
    private String name;
    private String email;
    private String avatarUrl;

    public Account(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Account(String name, String email, String avatarUrl) {
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public Account() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public String getDescription() {
        return name + "(" + email + ")";
    }
}
