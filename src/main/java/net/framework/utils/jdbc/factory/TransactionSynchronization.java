package net.framework.utils.jdbc.factory;

public interface TransactionSynchronization {
    void afterCommit();
}
