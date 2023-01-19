package net.ishop.jdbc.repository;

import net.framework.annotations.jdbc.Insert;
import net.framework.annotations.jdbc.Select;
import net.ishop.entities.Account;

public interface AccountRepository {
    String QUERY_FIND_BY_EMAIL = "SELECT * FROM account WHERE email = ?";

    @Insert
    void createAccount(Account account);
    @Select(sqlQuery = QUERY_FIND_BY_EMAIL)
    Account findByEmail(String email);
}
