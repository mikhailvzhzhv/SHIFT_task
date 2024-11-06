package mikhail.crm.service;

import mikhail.crm.api.error.RequestError;
import mikhail.crm.dto.transaction.TransactionReq;
import mikhail.crm.dto.transaction.TransactionRes;
import mikhail.crm.entity.Seller;
import mikhail.crm.entity.Transaction;
import mikhail.crm.repository.SellersRepository;
import mikhail.crm.repository.TransactionsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionsService {
    private final TransactionsRepository transactionRepo;
    private final SellersRepository sellerRepo;

    public TransactionsService(TransactionsRepository transactionRepo, SellersRepository sellerRepo) {
        this.transactionRepo = transactionRepo;
        this.sellerRepo = sellerRepo;
    }

    public ResponseEntity<?> getTransactions() {
        List<Transaction> transactions = transactionRepo.findAll();
        List<TransactionRes> transactionResps = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionResps.add(new TransactionRes(transaction));
        }
        return new ResponseEntity<>(transactionResps, HttpStatus.OK);
    }

    public ResponseEntity<?> createTransaction(TransactionReq transactionReq) {
        Optional<Seller> optSeller = sellerRepo.findById(transactionReq.getSellerId());
        if (optSeller.isEmpty()) {
            return new ResponseEntity<>(
                    new RequestError("Seller with id: " + transactionReq.getSellerId() + " not found"),
                    HttpStatus.BAD_REQUEST
            );
        }
        String payment_type = transactionReq.getPaymentType();
        System.out.println(payment_type.equals("CASH"));
        if (!payment_type.equals("CASH") && !payment_type.equals("CARD") && !payment_type.equals("TRANSFER)")) {
            return new ResponseEntity<>(
                    new RequestError("Unknown payment type: " + payment_type),
                    HttpStatus.BAD_REQUEST
            );
        }
        Seller seller = optSeller.get();
        transactionReq.setTransactionDate(LocalDateTime.now());
        Transaction t = new Transaction(transactionReq, seller);
        transactionRepo.save(t);
        return new ResponseEntity<>(new TransactionRes(t), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTransaction(long transactionId) {
        Optional<Transaction> t = transactionRepo.findById(transactionId);
        if (t.isEmpty()) {
            return new ResponseEntity<>(
                    new RequestError("Transaction with id: " + transactionId + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new TransactionRes(t.get()), HttpStatus.OK);
    }
}
