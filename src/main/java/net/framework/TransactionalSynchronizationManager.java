package net.framework;

import net.framework.exception.FrameworkSystemException;
import net.framework.utils.jdbc.factory.TransactionSynchronization;

import java.util.ArrayList;
import java.util.List;

public class TransactionalSynchronizationManager {
    private static final ThreadLocal<List<TransactionSynchronization>> transactionSynchronizations = new ThreadLocal<>();

    public static void initTransactionSynchronizationsLocalVariable() {
        transactionSynchronizations.set(new ArrayList<>());
    }

    public static List<TransactionSynchronization> getTransactionSynchronizationsLocalVariable() {
        return transactionSynchronizations.get();
    }

    public static void addTransactionSynchronization(TransactionSynchronization transactionSynchronization) {
        List<TransactionSynchronization> transactionsList = getTransactionSynchronizationsLocalVariable();
        if (transactionsList == null) {
            throw new FrameworkSystemException("TransactionSynchronization is null. " +
                    "Does your service method have annotation @Transaction(readOnly = false)?");
        } else {
            transactionsList.add(transactionSynchronization);
        }
    }

    public static void clearTransactionSynchronizations() {
        transactionSynchronizations.remove();
    }


}
