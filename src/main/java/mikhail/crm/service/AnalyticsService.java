package mikhail.crm.service;

import mikhail.crm.api.error.RequestError;
import mikhail.crm.dto.seller.SellerRes;
import mikhail.crm.entity.Seller;
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
public class AnalyticsService {
    private final TransactionsRepository transactionRepo;
    private final SellersRepository sellerRepo;

    public AnalyticsService(TransactionsRepository transactionRepo, SellersRepository sellerRepo) {
        this.transactionRepo = transactionRepo;
        this.sellerRepo = sellerRepo;
    }

    public ResponseEntity<?> getTopSellerByPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        Optional<Long> optSellerId = transactionRepo.getTopSellerByPeriod(startTime, endTime);
        if (optSellerId.isEmpty()) {
            return new ResponseEntity<>(new RequestError("Cannot find top seller with params"), HttpStatus.BAD_REQUEST);
        }
        Optional<Seller> optSeller = sellerRepo.findById(optSellerId.get());
        if (optSeller.isEmpty()) {
            return new ResponseEntity<>(
                    new RequestError("Top seller not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new SellerRes(optSeller.get()), HttpStatus.OK);
    }

    public ResponseEntity<?> getLooserSellersByPeriod(LocalDateTime startTime, LocalDateTime endTime, long amount) {
        List<Long> sellerIds = transactionRepo.getTopSellerLowerAmountByPeriod(startTime, endTime, amount);
        List<Seller> sellers = sellerRepo.findAllById(sellerIds);
        List<SellerRes> sellersResps = new ArrayList<>();
        for (Seller seller : sellers) {
            sellersResps.add(new SellerRes(seller));
        }
        return new ResponseEntity<>(sellersResps, HttpStatus.OK);
    }
}
