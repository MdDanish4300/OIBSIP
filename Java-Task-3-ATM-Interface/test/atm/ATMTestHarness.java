package atm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Simple no-dependency regression checks for the ATM project. */
public class ATMTestHarness {
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        runTest("Account deposit and withdrawal update balance and history",
                ATMTestHarness::testAccountTransactions);
        runTest("Account rejects zero and negative amounts",
                ATMTestHarness::testAccountRejectsNonPositiveAmounts);
        runTest("Account rejects withdrawal with insufficient funds",
                ATMTestHarness::testAccountInsufficientFunds);
        runTest("Transaction history is immutable to callers",
                ATMTestHarness::testHistoryIsImmutable);
        runTest("Bank authenticates valid credentials and rejects invalid credentials",
                ATMTestHarness::testAuthentication);
        runTest("Bank transfer updates both balances and records both sides",
                ATMTestHarness::testSuccessfulTransfer);
        runTest("Bank rejects invalid transfers without changing balances or history",
                ATMTestHarness::testFailedTransfers);
        runTest("Console denies login after three incorrect PIN attempts",
                ATMTestHarness::testLoginAttemptLimit);
        runTest("Console handles invalid menu choices and quits",
                ATMTestHarness::testMenuAndQuit);
        runTest("Console deposit, withdrawal, and history workflow",
                ATMTestHarness::testConsoleTransactionsAndHistory);
        runTest("Console successful transfer displays both updated balances",
                ATMTestHarness::testConsoleTransfer);
        runTest("Console rejects missing recipient and insufficient funds",
                ATMTestHarness::testConsoleTransferFailures);
        runTest("Console rejects invalid, zero, and negative amounts",
                ATMTestHarness::testConsoleInvalidAmounts);

        System.out.println();
        System.out.println("Test summary: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testAccountTransactions() {
        Account account = new Account("test-user", "1357", money("100.00"));

        assertTrue(account.deposit(money("25.50")), "positive deposit should succeed");
        assertMoney("125.50", account.getBalance(), "balance after deposit");
        assertTrue(account.withdraw(money("10.25")), "affordable withdrawal should succeed");
        assertMoney("115.25", account.getBalance(), "balance after withdrawal");

        List<Transaction> history = account.getTransactionHistory();
        assertEquals(2, history.size(), "two successful operations should be recorded");
        assertEquals("Deposit", history.get(0).getType(), "first transaction type");
        assertEquals("Withdrawal", history.get(1).getType(), "second transaction type");
    }

    private static void testAccountRejectsNonPositiveAmounts() {
        Account account = new Account("test-user", "1357", money("100.00"));

        assertFalse(account.deposit(BigDecimal.ZERO), "zero deposit should fail");
        assertFalse(account.deposit(money("-1.00")), "negative deposit should fail");
        assertFalse(account.withdraw(BigDecimal.ZERO), "zero withdrawal should fail");
        assertFalse(account.withdraw(money("-1.00")), "negative withdrawal should fail");
        assertMoney("100.00", account.getBalance(), "balance after rejected amounts");
        assertEquals(0, account.getTransactionHistory().size(), "failed operations are not recorded");
    }

    private static void testAccountInsufficientFunds() {
        Account account = new Account("test-user", "1357", money("100.00"));

        assertFalse(account.withdraw(money("100.01")), "withdrawal beyond balance should fail");
        assertMoney("100.00", account.getBalance(), "balance after rejected withdrawal");
        assertEquals(0, account.getTransactionHistory().size(), "failed withdrawal is not recorded");
    }

    private static void testHistoryIsImmutable() {
        Account account = new Account("test-user", "1357", money("100.00"));
        account.deposit(money("1.00"));

        boolean rejected = false;
        try {
            account.getTransactionHistory().clear();
        } catch (UnsupportedOperationException expected) {
            rejected = true;
        }
        assertTrue(rejected, "returned history should not be mutable");
        assertEquals(1, account.getTransactionHistory().size(), "account history remains intact");
    }

    private static void testAuthentication() {
        Bank bank = new Bank();

        assertTrue(bank.authenticate("user1001", "1234") != null, "valid login should succeed");
        assertTrue(bank.authenticate("user1001", "0000") == null, "wrong PIN should fail");
        assertTrue(bank.authenticate("missing-user", "1234") == null,
                "unknown user should fail");
        assertTrue(bank.findAccount("user1002") != null, "existing user should be found");
        assertTrue(bank.findAccount("missing-user") == null, "unknown user lookup should be null");
    }

    private static void testSuccessfulTransfer() {
        Bank bank = new Bank();
        Account sender = bank.findAccount("user1001");
        Account recipient = bank.findAccount("user1002");

        assertTrue(bank.transfer("user1001", "user1002", money("125.00")),
                "valid transfer should succeed");
        assertMoney("875.00", sender.getBalance(), "sender balance after transfer");
        assertMoney("625.00", recipient.getBalance(), "recipient balance after transfer");
        assertEquals(1, sender.getTransactionHistory().size(), "sender transfer record");
        assertEquals(1, recipient.getTransactionHistory().size(), "recipient transfer record");
        assertEquals("Withdrawal", sender.getTransactionHistory().get(0).getType(),
                "sender transfer history entry");
        assertEquals("Deposit", recipient.getTransactionHistory().get(0).getType(),
                "recipient transfer history entry");
    }

    private static void testFailedTransfers() {
        assertTransferRejectedWithoutChanges("user1001", "user1002", BigDecimal.ZERO,
                "zero amount should fail");
        assertTransferRejectedWithoutChanges("user1001", "user1002", money("-1.00"),
                "negative amount should fail");
        assertTransferRejectedWithoutChanges("user1001", "missing-user", money("1.00"),
                "unknown recipient should fail");
        assertTransferRejectedWithoutChanges("user1001", "user1001", money("1.00"),
                "transfer to the same account should fail");
        assertTransferRejectedWithoutChanges("user1001", "user1002", money("1000.01"),
                "insufficient funds should fail");
        assertTransferRejectedWithoutChanges("missing-user", "user1002", money("1.00"),
                "unknown sender should fail");
    }

    private static void assertTransferRejectedWithoutChanges(
            String senderId, String recipientId, BigDecimal amount, String message) {
        Bank bank = new Bank();
        Account sender = bank.findAccount("user1001");
        Account recipient = bank.findAccount("user1002");
        BigDecimal senderBefore = sender.getBalance();
        BigDecimal recipientBefore = recipient.getBalance();

        assertFalse(bank.transfer(senderId, recipientId, amount), message);
        assertMoney(senderBefore.toPlainString(), sender.getBalance(), message + " (sender unchanged)");
        assertMoney(recipientBefore.toPlainString(), recipient.getBalance(),
                message + " (recipient unchanged)");
        assertEquals(0, sender.getTransactionHistory().size(), message + " (sender history unchanged)");
        assertEquals(0, recipient.getTransactionHistory().size(),
                message + " (recipient history unchanged)");
    }

    private static void testLoginAttemptLimit() {
        Bank bank = new Bank();
        ConsoleResult result = runConsole(bank,
                "user1001\n0000\nuser1001\n0000\nuser1001\n0000\n");

        assertContains(result.output(), "Access denied. Three incorrect login attempts.",
                "three failures should deny access");
        assertFalse(result.output().contains("===== ATM Menu ====="),
                "menu should not appear after failed login");
    }

    private static void testMenuAndQuit() {
        ConsoleResult result = runConsole(new Bank(), "user1001\n1234\n0\n1\n5\n");

        assertContains(result.output(), "User ID:", "startup should request a user ID");
        assertContains(result.output(), "PIN:", "startup should request a PIN");
        assertContains(result.output(), "Login successful", "valid credentials should allow access");
        assertContains(result.output(), "1. Transaction History", "history option should display");
        assertContains(result.output(), "2. Withdraw", "withdraw option should display");
        assertContains(result.output(), "3. Deposit", "deposit option should display");
        assertContains(result.output(), "4. Transfer", "transfer option should display");
        assertContains(result.output(), "5. Quit", "quit option should display");
        assertContains(result.output(), "Invalid menu choice", "invalid menu input should be handled");
        assertContains(result.output(), "Thank you for using the ATM. Goodbye.", "quit should exit");
    }

    private static void testConsoleTransactionsAndHistory() {
        Bank bank = new Bank();
        ConsoleResult result = runConsole(bank,
                "user1001\n1234\n3\n75.00\n2\n20.00\n1\n5\n");
        Account account = bank.findAccount("user1001");

        assertMoney("1055.00", account.getBalance(), "console deposit and withdrawal balance");
        assertEquals(2, account.getTransactionHistory().size(), "console transaction history size");
        assertContains(result.output(), "Deposit successful", "deposit success should display");
        assertContains(result.output(), "Withdrawal successful", "withdrawal success should display");
        assertContains(result.output(), "Updated balance: 1075.00", "deposit balance should display");
        assertContains(result.output(), "Updated balance: 1055.00", "withdrawal balance should display");
        assertContains(result.output(), "Deposit | Amount: 75.00", "history should show deposit");
        assertContains(result.output(), "Withdrawal | Amount: 20.00", "history should show withdrawal");
    }

    private static void testConsoleTransfer() {
        Bank bank = new Bank();
        ConsoleResult result = runConsole(bank,
                "user1001\n1234\n4\nuser1002\n125.00\n5\n");

        assertMoney("875.00", bank.findAccount("user1001").getBalance(), "console sender balance");
        assertMoney("625.00", bank.findAccount("user1002").getBalance(), "console recipient balance");
        assertContains(result.output(), "Transfer successful to user1002", "transfer success should display");
        assertContains(result.output(), "Your updated balance: 875.00", "sender balance should display");
        assertContains(result.output(), "Recipient's updated balance: 625.00",
                "recipient balance should display");
    }

    private static void testConsoleTransferFailures() {
        Bank missingRecipientBank = new Bank();
        ConsoleResult missingRecipient = runConsole(missingRecipientBank,
                "user1001\n1234\n4\nmissing-user\n5\n");
        assertContains(missingRecipient.output(), "No account was found",
                "console should explain unknown recipient");
        assertMoney("1000.00", missingRecipientBank.findAccount("user1001").getBalance(),
                "unknown recipient leaves sender unchanged");

        Bank insufficientBank = new Bank();
        ConsoleResult insufficient = runConsole(insufficientBank,
                "user1001\n1234\n2\n1500.00\n4\nuser1002\n1500.00\n5\n");
        assertEquals(2, countOccurrences(insufficient.output(), "Insufficient Funds"),
                "withdrawal and transfer should report insufficient funds");
        assertMoney("1000.00", insufficientBank.findAccount("user1001").getBalance(),
                "failed console operations leave sender unchanged");
        assertMoney("500.00", insufficientBank.findAccount("user1002").getBalance(),
                "failed console transfer leaves recipient unchanged");
    }

    private static void testConsoleInvalidAmounts() {
        Bank bank = new Bank();
        ConsoleResult result = runConsole(bank,
                "user1001\n1234\n3\nnot-a-number\n3\n0\n2\n-5\n4\nuser1002\n-1\n5\n");
        Account account = bank.findAccount("user1001");

        assertEquals(4, countOccurrences(result.output(), "Invalid amount"),
                "bad, zero, and negative amounts should be rejected");
        assertMoney("1000.00", account.getBalance(), "invalid amounts leave balance unchanged");
        assertEquals(0, account.getTransactionHistory().size(),
                "invalid amounts are not recorded as transactions");
        assertEquals(0, bank.findAccount("user1002").getTransactionHistory().size(),
                "failed transfer creates no recipient history entry");
    }

    private static ConsoleResult runConsole(Bank bank, String input) {
        java.io.InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        try (PrintStream testOutput = new PrintStream(capturedOutput, true, StandardCharsets.UTF_8)) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(testOutput);
            new ATM(bank).start();
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return new ConsoleResult(capturedOutput.toString(StandardCharsets.UTF_8));
    }

    private static BigDecimal money(String value) {
        return new BigDecimal(value);
    }

    private static int countOccurrences(String text, String search) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(search, index)) >= 0) {
            count++;
            index += search.length();
        }
        return count;
    }

    private static void runTest(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + name);
        } catch (AssertionError | RuntimeException failure) {
            failed++;
            System.out.println("FAIL: " + name + " - " + failure.getMessage());
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static void assertMoney(String expected, BigDecimal actual, String message) {
        if (actual == null || money(expected).compareTo(actual) != 0) {
            throw new AssertionError(message + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }

    private static void assertContains(String text, String expected, String message) {
        if (!text.contains(expected)) {
            throw new AssertionError(message + ": output did not contain <" + expected + ">");
        }
    }

    private record ConsoleResult(String output) {
    }
}
