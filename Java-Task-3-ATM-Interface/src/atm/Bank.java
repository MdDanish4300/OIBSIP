package atm;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores accounts and coordinates authentication and transfers.
 *
 * <p>Demo credentials for trying the application:
 * <ul>
 *   <li>User ID {@code user1001}, PIN {@code 1234}, opening balance 1000.00</li>
 *   <li>User ID {@code user1002}, PIN {@code 2345}, opening balance 500.00</li>
 *   <li>User ID {@code user1003}, PIN {@code 3456}, opening balance 250.00</li>
 * </ul>
 */
public class Bank {
    private final Map<String, Account> accounts;

    public Bank() {
        this.accounts = new LinkedHashMap<>();

        // Sample accounts are kept in memory for local testing.
        addAccount(new Account("user1001", "1234", new BigDecimal("1000.00")));
        addAccount(new Account("user1002", "2345", new BigDecimal("500.00")));
        addAccount(new Account("user1003", "3456", new BigDecimal("250.00")));
    }

    /** Adds an account to the in-memory bank. */
    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }

    /**
     * Authenticates a user and returns the matching account when the PIN is
     * correct. Returns null when the user ID is unknown or the PIN is wrong.
     */
    public Account authenticate(String userId, String pin) {
        Account account = findAccount(userId);
        if (account == null || !account.validatePin(pin)) {
            return null;
        }
        return account;
    }

    /** Finds an account by its user ID, or returns null when it is unknown. */
    public Account findAccount(String userId) {
        return accounts.get(userId);
    }

    /**
     * Transfers money between two different accounts. The existing account
     * methods record the sender's debit as a withdrawal and the recipient's
     * credit as a deposit in their respective transaction histories.
     *
     * @return true when the transfer succeeds, or false when either account
     *         is unknown, both IDs identify the same account, the amount is
     *         not positive, or the sender lacks funds
     */
    public boolean transfer(String senderId, String recipientId, BigDecimal amount) {
        Account sender = findAccount(senderId);
        Account recipient = findAccount(recipientId);

        if (sender == null || recipient == null) {
            return false;
        }
        if (sender == recipient) {
            return false;
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (sender.getBalance() == null || recipient.getBalance() == null) {
            return false;
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            return false;
        }

        // Both operations are expected to pass after the checks above.
        if (!sender.withdraw(amount)) {
            return false;
        }
        return recipient.deposit(amount);
    }
}
