package atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Stores one user's credentials, balance, and current-session history. */
public class Account {
    private final String userId;
    private final String pin;
    private BigDecimal balance;
    private final ArrayList<Transaction> transactionHistory;

    public Account(String userId, String pin, BigDecimal openingBalance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = openingBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /** Checks whether the entered PIN matches this account's PIN. */
    public boolean validatePin(String enteredPin) {
        return pin.equals(enteredPin);
    }

    /**
     * Adds money to the balance and records the deposit.
     *
     * @return true when the deposit succeeds, or false for a missing or
     *         non-positive amount
     */
    public boolean deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        balance = balance.add(amount);
        recordTransaction(new Transaction("Deposit", amount, "Cash deposit"));
        return true;
    }

    /**
     * Removes money from the balance and records the withdrawal.
     *
     * @return true when the withdrawal succeeds, or false for a missing,
     *         non-positive, or unaffordable amount
     */
    public boolean withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        if (balance.compareTo(amount) < 0) {
            return false;
        }

        balance = balance.subtract(amount);
        recordTransaction(new Transaction("Withdrawal", amount, "Cash withdrawal"));
        return true;
    }

    /** Returns an immutable snapshot so callers cannot change account history. */
    public List<Transaction> getTransactionHistory() {
        return List.copyOf(transactionHistory);
    }

    /** Internal helper for recording completed operations. */
    void recordTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}
