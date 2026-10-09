# Java Task 3: ATM Interface

## Project overview

A console-based ATM simulation written in Java. It demonstrates basic object-oriented design through account authentication, transaction handling, and an interactive menu.

## Technologies

- Java 21
- Git for version control
- Core Java, including `BigDecimal`, collections, and console input/output

## Implemented features

- Sign in with a User ID and PIN; access is denied after three failed attempts.
- View the signed-in account's transaction history.
- Deposit and withdraw money with positive-amount validation.
- Transfer money between the demo accounts.
- Check available funds before withdrawals and transfers, and report **Insufficient Funds** when the balance is too low.
- Reject invalid menu selections, invalid amounts, unknown recipients, and transfers to the same account.
- Show updated balances and record successful account operations in transaction history.

## Production classes

The application source is in `src/atm/`:

| Class | Responsibility |
| --- | --- |
| `Main` | Creates the in-memory `Bank` and starts the `ATM`. |
| `ATM` | Reads console input, authenticates users, displays the menu, and handles the interactive flow and messages. |
| `Account` | Stores a user's credentials and balance; applies deposits and withdrawals and keeps transaction history in an `ArrayList`. |
| `Bank` | Stores demo accounts, authenticates credentials, looks up accounts, and coordinates transfers. |
| `Transaction` | Represents a completed operation with its type, amount, details, and time. |

Transfers are recorded in the sender's history as a withdrawal and in the recipient's history as a deposit.

## Demo accounts

These credentials are defined in `Bank`:

| User ID | PIN | Opening balance |
| --- | --- | ---: |
| `user1001` | `1234` | 1000.00 |
| `user1002` | `2345` | 500.00 |
| `user1003` | `3456` | 250.00 |

## Compile and run the application

From this task directory in PowerShell, compile to a temporary directory and start the program:

```powershell
$buildDir = Join-Path $env:TEMP 'oibsip-atm-build'
New-Item -ItemType Directory -Force -Path $buildDir | Out-Null
javac --release 21 -d $buildDir src\atm\*.java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
java -cp $buildDir atm.Main
```

## Automated test harness

The no-dependency harness is located at `test/atm/ATMTestHarness.java`. It checks account operations and history, authentication, transfer validation, login lockout, menu behavior, and scripted console transactions and failures.

Compile and run the harness from this task directory in PowerShell:

```powershell
$testBuildDir = Join-Path $env:TEMP 'oibsip-atm-test-build'
New-Item -ItemType Directory -Force -Path $testBuildDir | Out-Null
javac --release 21 -d $testBuildDir src\atm\*.java test\atm\ATMTestHarness.java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
java -cp $testBuildDir atm.ATMTestHarness
```

No test result is asserted in this README. The harness was not run as part of this documentation update; use the command above to check the current code.

## Current limitations

- Demo accounts and balances are held in memory by `Bank`. They reset to their opening values whenever the application restarts.
- Transaction history is also held in memory and is available only during the current application run.
- The project is a console simulation. It does not connect to a database or a real banking service.
- Demo credentials are stored directly in the source code and are for local demonstration only.

## Screenshots and internship submission checklist

No screenshot, demo video, or internship submission evidence is recorded here yet.

- [ ] Capture and add screenshots to `screenshots/`.
- [ ] Record and save a demo video, if required for the internship submission.
- [ ] Complete the required internship submission steps and retain confirmation evidence.
