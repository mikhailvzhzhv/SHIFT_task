package mikhail.crm.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import mikhail.crm.entity.Transaction;

import java.time.LocalDateTime;

@Data
public class TransactionRes {
    private long id;
    private long amount;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("transaction_date")
    private LocalDateTime transactionDate;

    @JsonProperty("seller_id")
    private long sellerId;

    public TransactionRes(Transaction t) {
        id = t.getId();
        amount = t.getAmount();
        paymentType = t.getPaymentType();
        transactionDate = t.getTransactionDate();
        sellerId = t.getSeller().getId();
    }
}
