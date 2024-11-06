package mikhail.crm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import mikhail.crm.dto.transaction.TransactionReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "transaction_date", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    public Transaction(TransactionReq t, Seller s) {
        amount = t.getAmount();
        paymentType = t.getPaymentType();
        transactionDate = t.getTransactionDate();
        seller = s;
    }
}
