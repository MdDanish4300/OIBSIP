package atm;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/** Handles console input and the ATM user flow. */
public class ATM {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final DateTimeFormatter TRANSACTION_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Bank bank;
    private final Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    /** Starts authentication and shows the menu after a successful login. */
    public void start() {
        Account account = authenticateUser();
        if (account == null) {
            return;
        }

        System.out.println("\nLogin successful. Welcome, " + account.getUserId() + ".");
        runMenu(account);
    }

    private Account authenticateUser() {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            String userId = readLine("User ID: ");
            if (userId == null) {
                System.out.println("Input ended. Exiting ATM.");
                return null;
            }

            String pin = readLine("PIN: ");
            if (pin == null) {
                System.out.println("Input ended. Exiting ATM.");
                return null;
            }

            Account account = bank.authenticate(userId.trim(), pin.trim());
            if (account != null) {
                return account;
            }

            int attemptsRemaining = MAX_LOGIN_ATTEMPTS - attempt;
            if (attemptsRemaining > 0) {
                System.out.println("Incorrect User ID or PIN. Attempts remaining: "
                        + attemptsRemaining + ".");
            }
        }

        System.out.println("Access denied. Three incorrect login attempts. Exiting ATM.");
        return null;
    }

    private void runMenu(Account account) {
        boolean isRunning = true;
        while (isRunning) {
            showMenu();
            String choice = readLine("Choose an option: ");
            if (choice == null) {
                System.out.println("Input ended. Exiting ATM.");
                return;
            }

            switch (choice.trim()) {
                case "1" -> showTransactionHistory(account);
                case "2" -> withdraw(account);
                case "3" -> deposit(account);
                case "4" -> transfer(account);
                case "5" -> {
                    System.out.println("Thank you for using the ATM. Goodbye.");
                    isRunning = false;
                }
                default -> System.out.println("Invalid menu choice. Please choose 1 through 5.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n===== ATM Menu =====");
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    private void showTransactionHistory(Account account) {
        System.out.println("\n===== Transaction History =====");
        List<Transaction> history = account.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions have been recorded this session.");
            return;
        }

        for (Transaction transaction : history) {
            System.out.println(transaction.getTime().format(TRANSACTION_TIME_FORMAT)
                    + " | " + transaction.getType()
                    + " | Amount: " + transaction.getAmount().toPlainString()
                    + " | " + transaction.getDetails());
        }
    }

    private void withdraw(Account account) {
        BigDecimal amount = readPositiveAmount("Withdrawal amount: ");
        if (amount == null) {
            return;
        }

        if (account.getBalance().compareTo(amount) < 0) {
            System.out.println("Insufficient Funds. Your balance is "
                    + account.getBalance().toPlainString() + ".");
            return;
        }

        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful. Updated balance: "
                    + account.getBalance().toPlainString() + ".");
        } else {
            System.out.println("Withdrawal failed. Please check the amount and try again.");
        }
    }

    private void deposit(Account account) {
        BigDecimal amount = readPositiveAmount("Deposit amount: ");
        if (amount == null) {
            return;
        }

        if (account.deposit(amount)) {
            System.out.println("Deposit successful. Updated balance: "
                    + account.getBalance().toPlainString() + ".");
        } else {
            System.out.println("Deposit failed. Please check the amount and try again.");
        }
    }

    private void transfer(Account sender) {
        String recipientId = readLine("Recipient User ID: ");
        if (recipientId == null) {
            System.out.println("Input ended. Transfer cancelled.");
            return;
        }
        recipientId = recipientId.trim();

        Account recipient = bank.findAccount(recipientId);
        if (recipient == null) {
            System.out.println("Transfer failed. No account was found for that User ID.");
            return;
        }
        if (recipient == sender) {
            System.out.println("Transfer failed. You cannot transfer to the same account.");
            return;
        }

        BigDecimal amount = readPositiveAmount("Transfer amount: ");
        if (amount == null) {
            return;
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            System.out.println("Insufficient Funds. Your balance is "
                    + sender.getBalance().toPlainString() + ".");
            return;
        }

        if (bank.transfer(sender.getUserId(), recipientId, amount)) {
            System.out.println("Transfer successful to " + recipientId + ".");
            System.out.println("Your updated balance: " + sender.getBalance().toPlainString());
            System.out.println("Recipient's updated balance: "
                    + recipient.getBalance().toPlainString() + ".");
        } else {
            System.out.println("Transfer failed. Check the recipient and available balance.");
        }
    }

    /** Reads a number and rejects invalid, zero, or negative amounts. */
    private BigDecimal readPositiveAmount(String prompt) {
        String input = readLine(prompt);
        if (input == null) {
            System.out.println("Input ended. Operation cancelled.");
            return null;
        }

        try {
            BigDecimal amount = new BigDecimal(input.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Invalid amount. Enter a number greater than zero.");
                return null;
            }
            return amount;
        } catch (NumberFormatException exception) {
            System.out.println("Invalid amount. Enter a valid number, such as 25.00.");
            return null;
        }
    }

    /** Reads one line, returning null when the input stream has ended. */
    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }
}
