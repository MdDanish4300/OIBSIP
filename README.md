# Oasis Infobyte Java Development Internship

This repository contains selected Java Development Internship projects for Oasis Infobyte (OIBSIP). Each project is kept in its own task directory with its documentation, source, and evidence folders.

## Project status

| Task | Project | Status |
| --- | --- | --- |
| Java Task 1 | [Online Reservation System](Java-Task-1-Online-Reservation-System/) | Not started — application implementation has not started yet. |
| Java Task 3 | [ATM Interface](Java-Task-3-ATM-Interface/) | Implemented — testing and submission evidence still need final verification. |

## ATM Interface features

The console ATM application includes:

- Login with a User ID and PIN, with access denied after three failed attempts.
- Transaction history for the current application session.
- Deposits and withdrawals with amount validation.
- Transfers between accounts with input and insufficient-funds checks.

A previous report recorded 13 automated tests passed and 0 failed. That result has not been independently verified during this README update.

## Repository structure

```text
OIBSIP/
├── AGENTS.md
├── README.md
├── Java-Task-1-Online-Reservation-System/
│   ├── README.md
│   ├── src/
│   ├── screenshots/
│   └── output/
└── Java-Task-3-ATM-Interface/
    ├── .gitignore
    ├── README.md
    ├── src/
    │   └── atm/
    │       ├── Account.java
    │       ├── ATM.java
    │       ├── Bank.java
    │       ├── Main.java
    │       └── Transaction.java
    ├── test/
    │   └── atm/
    │       └── ATMTestHarness.java
    ├── screenshots/
    └── output/
```

Each task has its own README, source directory, screenshots directory, and output directory. Refer to the task README for its requirements, implementation details, and current progress.

Do not commit generated build files, such as Java `.class` files or compiled output directories.
