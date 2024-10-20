package mikhail.crm;
import mikhail.crm.api.error.RequestError;
import mikhail.crm.dto.transaction.TransactionReq;
import mikhail.crm.dto.transaction.TransactionRes;
import mikhail.crm.entity.Seller;
import mikhail.crm.entity.Transaction;
import mikhail.crm.repository.SellersRepository;
import mikhail.crm.repository.TransactionsRepository;
import mikhail.crm.service.TransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class TransactionsServiceTests {

    @Autowired
    private TransactionsService transactionsService;

    @MockBean
    private TransactionsRepository transactionsRepository;

    @MockBean
    private SellersRepository sellersRepository;

    private Seller testSeller;
    private Transaction testTransaction;

    @BeforeEach
    void init() {
        testSeller = new Seller();
        testSeller.setId(1L);
        testSeller.setName("Mikhail");
        testSeller.setContactInfo("mvzhzhv@gmail.com");
        testSeller.setRegistrationDate(LocalDateTime.now());

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setSeller(testSeller);
        testTransaction.setAmount(10000);
        testTransaction.setPaymentType("CASH");
        testTransaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void testGetTransactions_Success() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(testTransaction);

        when(transactionsRepository.findAll()).thenReturn(transactions);

        ResponseEntity<?> response = transactionsService.getTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<TransactionRes> transactionResList = (List<TransactionRes>) response.getBody();
        assertNotNull(transactionResList);
        assertEquals(1, transactionResList.size());
        assertEquals(testTransaction.getAmount(), transactionResList.get(0).getAmount());
    }

    @Test
    void testCreateTransaction_Success() {
        TransactionReq transactionReq = new TransactionReq();
        transactionReq.setSellerId(1L);
        transactionReq.setAmount(10000);
        transactionReq.setPaymentType("CASH");

        when(sellersRepository.findById(1L)).thenReturn(Optional.of(testSeller));
        when(transactionsRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        ResponseEntity<?> response = transactionsService.createTransaction(transactionReq);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        TransactionRes transactionRes = (TransactionRes) response.getBody();
        assertNotNull(transactionRes);
        assertEquals(testTransaction.getAmount(), transactionRes.getAmount());
    }

    @Test
    void testCreateTransaction_SellerNotFound() {
        TransactionReq transactionReq = new TransactionReq();
        transactionReq.setSellerId(1L);
        transactionReq.setAmount(10000);
        transactionReq.setPaymentType("CASH");

        when(sellersRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = transactionsService.createTransaction(transactionReq);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Seller with id: 1 not found", error.getMessage());
    }

    @Test
    void testCreateTransaction_UnknownPaymentType() {
        TransactionReq transactionReq = new TransactionReq();
        transactionReq.setSellerId(1L);
        transactionReq.setAmount(10000);
        transactionReq.setPaymentType("GADOST");

        when(sellersRepository.findById(1L)).thenReturn(Optional.of(testSeller));

        ResponseEntity<?> response = transactionsService.createTransaction(transactionReq);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Unknown payment type: GADOST", error.getMessage());
    }

    @Test
    void testGetTransaction_Success() {
        when(transactionsRepository.findById(1L)).thenReturn(Optional.of(testTransaction));

        ResponseEntity<?> response = transactionsService.getTransaction(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TransactionRes transactionRes = (TransactionRes) response.getBody();
        assertNotNull(transactionRes);
        assertEquals(testTransaction.getAmount(), transactionRes.getAmount());
    }

    @Test
    void testGetTransaction_NotFound() {
        when(transactionsRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = transactionsService.getTransaction(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Transaction with id: 1 not found", error.getMessage());
    }
}
