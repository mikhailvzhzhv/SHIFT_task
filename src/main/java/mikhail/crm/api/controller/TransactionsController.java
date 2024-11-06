package mikhail.crm.api.controller;

import mikhail.crm.dto.transaction.TransactionReq;
import mikhail.crm.service.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    public TransactionsController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getTransactions() {
        return transactionsService.getTransactions();
    }

    @PostMapping("/")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionReq transaction) {
        return transactionsService.createTransaction(transaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransactions(@PathVariable long transactionId) {
        return transactionsService.getTransaction(transactionId);
    }

}
