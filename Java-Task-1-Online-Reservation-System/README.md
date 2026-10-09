# Java Task 1: Online Reservation System

## Objective

Create a desktop application for booking and cancelling train reservations. This README records the required behavior from the Oasis Infobyte task description.

## Planned technology stack

- Java
- Java Swing for the desktop interface
- JDBC for database access
- SQLite for reservation storage

## Required features

### Login

- Show a login form with username and password fields.
- Deny access when the credentials are invalid.

### Reservation

- Provide fields for passenger name, train number, train name, class type, date of journey, source station, and destination station.
- Automatically populate the train name from the entered train number.
- Validate that required fields are filled, the journey date uses a valid date format, and the train number is numeric.
- Provide an **Insert/Book** action that saves the reservation to the database.
- Automatically generate a unique PNR number for each reservation.
- After a successful reservation, show a confirmation dialog with the booking details.

### Cancellation

- Provide a cancellation form with a PNR input and a **Fetch** button.
- Fetch and display the full booking details for the entered PNR.
- Ask the user to confirm cancellation before removing the booking from the database.

## Optional recommendations

- JavaFX may be used instead of Swing for the interface.
- MySQL may be used instead of SQLite for the database.

## Testing checklist

- [ ] Check that the login form accepts valid credentials and denies invalid credentials.
- [ ] Check that each required reservation field can be entered and required-field validation works.
- [ ] Check that the train name is populated from the train number.
- [ ] Check that invalid date formats and non-numeric train numbers are rejected.
- [ ] Check that **Insert/Book** stores the reservation and that each new reservation receives a unique PNR.
- [ ] Check that the success dialog displays the booking details.
- [ ] Check that **Fetch** looks up a PNR and displays all booking details.
- [ ] Check that cancellation asks for confirmation and removes the reservation only after confirmation.

## Implementation status

**Not started**

No application features have been implemented or tested yet. The `src/` directory is reserved for Java source code; `screenshots/` and `output/` are reserved for task evidence and generated output.
