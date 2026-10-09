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

## Testing checklist

- [ ] Check that startup asks for a User ID and PIN.
- [ ] Check that valid credentials allow access and three incorrect attempts deny access.
- [ ] Check that the menu shows Transaction History, Withdraw, Deposit, Transfer, and Quit.
- [ ] Check that deposits update the balance and appear in transaction history.
- [ ] Check that withdrawals with enough funds update the balance and appear in history.
- [ ] Check that a withdrawal with insufficient funds displays **Insufficient Funds**.
- [ ] Check that transfers with enough funds complete and appear in transaction history.
- [ ] Check that a transfer with insufficient funds displays **Insufficient Funds**.
- [ ] Check that Transaction History clearly displays all recorded transactions.
- [ ] Check that Quit exits the program.
- [ ] Confirm that the design has the required `ATM`, `Account`, `Transaction`, `Bank`, and `Main` classes.

## Implementation status

**Not started**

No application features have been implemented or tested yet. The `src/` directory is reserved for Java source code; `screenshots/` and `output/` are reserved for task evidence and generated output.
