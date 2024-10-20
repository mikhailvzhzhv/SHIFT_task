package mikhail.crm.repository;

import mikhail.crm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.seller.id = :sellerId")
    List<Transaction> getTransactionsBySellerId(@Param("sellerId") long sellerId);

    @Query("select t.seller.id from Transaction t where t.transactionDate between :startTime and :endTime group by t.seller.id order by sum(t.amount) desc limit 1")
    Optional<Long> getTopSellerByPeriod(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("select t.seller.id from Transaction t where t.transactionDate between :startTime and :endTime group by t.seller.id having sum(t.amount) < :amount")
    List<Long> getTopSellerLowerAmountByPeriod(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("amount") long amount);
}