package mikhail.crm.api.controller;

import mikhail.crm.dto.seller.SellerReq;
import mikhail.crm.service.SellersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
public class SellersController {
    private final SellersService sellersService;

    public SellersController(SellersService sellersService) {
        this.sellersService = sellersService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getSellers() {
        return sellersService.getSellers();
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<?> getSeller(@PathVariable long sellerId) {
        return sellersService.getSellerInfo(sellerId);
    }

    @PostMapping("/")
    public ResponseEntity<?> createSeller(@RequestBody SellerReq seller) {
        return sellersService.createSeller(seller);
    }

    @PatchMapping("/{sellerId}")
    public ResponseEntity<?> updateSeller(@PathVariable long sellerId, @RequestBody SellerReq seller) {
        return sellersService.updateSellerInfo(sellerId, seller);
    }

    @DeleteMapping("/{sellerId}")
    public ResponseEntity<?> deleteSeller(@PathVariable long sellerId) {
        return sellersService.deleteSeller(sellerId);
    }

    @GetMapping("/{sellerId}/transactions")
    public ResponseEntity<?> getSellerTransactions(@PathVariable long sellerId) {
        return sellersService.getSellerTransactions(sellerId);
    }
}
