package mikhail.crm.service;

import mikhail.crm.api.error.RequestError;
import mikhail.crm.dto.seller.SellerReq;
import mikhail.crm.dto.seller.SellerRes;
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
public class SellersService {
    private final SellersRepository sellerRepo;
    private final TransactionsRepository transactionRepo;

    public SellersService(SellersRepository sellerRepo, TransactionsRepository transactionRepo) {
        this.sellerRepo = sellerRepo;
        this.transactionRepo = transactionRepo;
    }

    public ResponseEntity<?> getSellers() {
        List<Seller> sellers = sellerRepo.findAll();
        List<SellerRes> sellersResps = new ArrayList<>();
        for (Seller seller : sellers) {
            sellersResps.add(new SellerRes(seller));
        }
        return new ResponseEntity<>(sellersResps, HttpStatus.OK);
    }

    public ResponseEntity<?> getSellerInfo(long sellerId) {
        Optional<Seller> optSeller = sellerRepo.findById(sellerId);
        if (optSeller.isEmpty()) {
            return new ResponseEntity<>(
                    new RequestError("Seller with id: " + sellerId + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new SellerRes(optSeller.get()), HttpStatus.OK);
    }

    public ResponseEntity<?> createSeller(SellerReq r) {
        r.setRegistrationDate(LocalDateTime.now());
        Seller seller = new Seller(r);
        sellerRepo.save(seller);
        return new ResponseEntity<>(new SellerRes(seller), HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateSellerInfo(long sellerId, SellerReq r) {
        Optional<Seller> optSeller = sellerRepo.findById(sellerId);
        if (optSeller.isEmpty()) {
            return new ResponseEntity<>(
                    new RequestError("Seller with id: " + sellerId + " not found"), HttpStatus.NOT_FOUND);
        }
        Seller seller = optSeller.get();
        seller.update(r);
        sellerRepo.save(seller);
        return new ResponseEntity<>(new SellerRes(seller), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteSeller(long sellerId) {
        Optional<Seller> optSeller = sellerRepo.findById(sellerId);
        if (optSeller.isEmpty()) {
            return new ResponseEntity<>(
                    new RequestError("Seller with id: " + sellerId + " not found"), HttpStatus.NOT_FOUND);
        }

        Seller seller = optSeller.get();
        sellerRepo.delete(seller);
        return new ResponseEntity<>(new SellerRes(seller), HttpStatus.OK);
    }

    public ResponseEntity<?> getSellerTransactions(long sellerId) {
        List<Transaction> transactions = transactionRepo.getTransactionsBySellerId(sellerId);
        List<TransactionRes> transactionResps = new ArrayList<>();
        for (Transaction t : transactions) {
            transactionResps.add(new TransactionRes(t));
        }
        return new ResponseEntity<>(transactionResps, HttpStatus.OK);
    }
}
