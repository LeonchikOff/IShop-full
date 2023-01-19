package net.ishop.entities;

import net.framework.annotations.jdbc.mapping.Column;
import net.framework.annotations.jdbc.mapping.Table;
import net.ishop.models.social.CurrentAccount;

@Table(nameOfTable = "account", generationNextIdExpresion = "nextval('account_seq')")
public class Account extends AbstractEntity<Integer> implements CurrentAccount {
    @Column(columnName = "name")
    private String name;
    @Column(columnName = "email")
    private String email;
    @Column(columnName = "avatar_url")
    private String avatarUrl;

    public Account() {
    }

    public Account(String name, String email, String avatarUrl) {
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
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
