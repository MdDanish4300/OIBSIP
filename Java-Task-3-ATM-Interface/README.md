# Java Task 3: ATM Interface

## Objective

Create a console-based ATM simulation in Java using object-oriented programming (OOP).

## Planned technology stack

- Java
- Core Java and OOP
- Console input and output
- `ArrayList` for transaction history

## Required features

### Authentication

- Ask for a User ID and PIN when the program starts.
- Deny access after three incorrect authentication attempts.

### ATM menu and operations

Show a main menu with these options:

1. Transaction History
2. Withdraw
3. Deposit
4. Transfer
5. Quit

- Check that the account has enough balance before a withdrawal or transfer.
- Display **Insufficient Funds** when the balance is too low for a withdrawal or transfer.
- Store all transactions in an `ArrayList` and display them clearly in Transaction History.

### Required classes

Use at least these five distinct classes:

- `ATM` — manages the ATM menu and user interaction.
- `Account` — stores account information and balance.
- `Transaction` — represents a deposit, withdrawal, or transfer.
- `Bank` — manages accounts and authentication.
- `Main` — starts the program.

## Optional recommendations

- Add input checks for invalid menu choices and non-positive transaction amounts.
- Show a clear result after each successful deposit, withdrawal, or transfer.

## Implementation

The console application is implemented in `src/atm/` with the required five
classes:

- `Main` starts the application.
- `ATM` handles login prompts, the three-attempt limit, menu choices, input
  validation, and user messages.
- `Bank` stores the in-memory accounts, authenticates users, and coordinates
  transfers.
- `Account` stores the user ID, PIN, balance, and current-session transaction
  history. Deposits and withdrawals reject non-positive amounts and record
  successful operations.
- `Transaction` stores the type, amount, details, and time of an operation.

The Bank constructor provides these demo accounts:

| User ID | PIN | Opening balance |
| --- | --- | ---: |
| `user1001` | `1234` | 1000.00 |
| `user1002` | `2345` | 500.00 |
| `user1003` | `3456` | 250.00 |

Successful transfers update both balances. With the current `Account` interface,
the sender's history records a withdrawal and the recipient's history records a
deposit. Both are transaction records for the transfer.

## Build and run with Java 21

From this task directory in PowerShell, compile into the system temporary
directory so generated class files are kept out of the repository:

```powershell
$buildDir = Join-Path $env:TEMP 'oibsip-atm-build'
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
javac --release 21 -d $buildDir src\atm\*.java
java -cp $buildDir atm.Main
```

At login, use one of the demo credential pairs above.

## Automated tests

`test/atm/ATMTestHarness.java` is a small Java test harness with no external
dependencies. It checks account balance/history behavior, Bank authentication
and transfer validation, login lockout, menu and quit behavior, and scripted
console transactions and failures.

Run the harness from this task directory in PowerShell:

```powershell
$testBuildDir = Join-Path $env:TEMP 'oibsip-atm-test-build'
New-Item -ItemType Directory -Force -Path $testBuildDir | Out-Null
javac --release 21 -d $testBuildDir src\atm\*.java test\atm\ATMTestHarness.java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
java -cp $testBuildDir atm.ATMTestHarness
```

## Testing checklist

- [x] Startup prompts for a User ID and PIN; valid credentials allow access.
- [x] Three incorrect PIN attempts deny access.
- [x] The menu shows Transaction History, Withdraw, Deposit, Transfer, and Quit.
- [x] Deposits update the balance and appear in transaction history.
- [x] Withdrawals with enough funds update the balance and appear in history.
- [x] A withdrawal with insufficient funds displays **Insufficient Funds**.
- [x] Successful transfers update both account balances and both histories.
- [x] Transfers with insufficient funds display **Insufficient Funds**.
- [x] Transfers reject missing recipients, the same account, and non-positive amounts.
- [x] Deposits and withdrawals reject zero and negative amounts; invalid numeric input is handled.
- [x] Transaction History clearly displays recorded transactions.
- [x] Invalid menu choices are handled, and Quit exits the program.
- [x] The required `ATM`, `Account`, `Transaction`, `Bank`, and `Main` classes compile.

**Verification result:** Compiled with Java 21 (`javac 21.0.11`, `--release 21`).
The automated harness completed with **13 passed, 0 failed**. Build output was
written to the system temporary directory.

## Implementation status

**Implemented and verified.** The `screenshots/` and `output/` directories remain
available for task evidence and generated output.
