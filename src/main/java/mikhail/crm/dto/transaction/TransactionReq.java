package mikhail.crm.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionReq {
    private long amount;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("transaction_date")
    private LocalDateTime transactionDate;

    @JsonProperty("seller_id")
    private long sellerId;
}
