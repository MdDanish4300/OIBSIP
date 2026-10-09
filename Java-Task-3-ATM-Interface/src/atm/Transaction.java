package atm;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Describes one completed account operation. */
public class Transaction {
    private final String type;
    private final BigDecimal amount;
    private final String details;
    private final LocalDateTime time;

    public Transaction(String type, BigDecimal amount, String details) {
        this.type = type;
        this.amount = amount;
        this.details = details;
        this.time = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
