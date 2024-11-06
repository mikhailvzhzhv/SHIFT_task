package mikhail.crm;

import mikhail.crm.api.error.RequestError;
import mikhail.crm.dto.seller.SellerReq;
import mikhail.crm.dto.seller.SellerRes;
import mikhail.crm.entity.Seller;
import mikhail.crm.repository.SellersRepository;
import mikhail.crm.repository.TransactionsRepository;
import mikhail.crm.service.SellersService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@SpringBootTest
class SellersServiceTests {

    @Autowired
    private SellersService sellersService;

    @MockBean
    private SellersRepository sellersRepository;

    private Seller testSeller;

    private Seller testUpdSeller;

    @BeforeEach
    void init() {
        testSeller = new Seller();
        testSeller.setId(1L);
        testSeller.setName("Mikhail");
        testSeller.setContactInfo("mvzhzhv@gmail.com");
        testSeller.setRegistrationDate(LocalDateTime.now());

        testUpdSeller = new Seller();
        testUpdSeller.setId(1L);
        testUpdSeller.setName("Sanya");
        testUpdSeller.setContactInfo("Sanya@gmail.com");
        testUpdSeller.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void testGetSellers_Success() {
        List<Seller> sellers = new ArrayList<>();
        sellers.add(testSeller);

        when(sellersRepository.findAll()).thenReturn(sellers);

        ResponseEntity<?> response = sellersService.getSellers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SellerRes> sellersResList = (List<SellerRes>) response.getBody();
        assertNotNull(sellersResList);
        assertEquals(1, sellersResList.size());
        assertEquals(testSeller.getName(), sellersResList.get(0).getName());
    }

    @Test
    void testGetSellerInfo_Success() {
        when(sellersRepository.findById(1L)).thenReturn(Optional.of(testSeller));

        ResponseEntity<?> response = sellersService.getSellerInfo(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SellerRes sellerRes = (SellerRes) response.getBody();
        assertNotNull(sellerRes);
        assertEquals(testSeller.getName(), sellerRes.getName());
    }

    @Test
    void testGetSellerInfo_NotFound() {
        when(sellersRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = sellersService.getSellerInfo(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Seller with id: 1 not found", error.getMessage());
    }

    @Test
    void testCreateSeller_Success() {
        SellerReq sellerReq = new SellerReq();
        sellerReq.setName("Mikhail");
        sellerReq.setContactInfo("mvzhzhv@gmail.com");

        when(sellersRepository.save(any(Seller.class))).thenReturn(testSeller);

        ResponseEntity<?> response = sellersService.createSeller(sellerReq);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        SellerRes sellerRes = (SellerRes) response.getBody();
        assertNotNull(sellerRes);
        assertEquals(testSeller.getName(), sellerRes.getName());
    }

    @Test
    void testDeleteSeller_Success() {
        when(sellersRepository.findById(1L)).thenReturn(Optional.of(testSeller));
        doNothing().when(sellersRepository).delete(testSeller);

        ResponseEntity<?> response = sellersService.deleteSeller(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SellerRes sellerRes = (SellerRes) response.getBody();
        assertNotNull(sellerRes);
        assertEquals(testSeller.getName(), sellerRes.getName());
    }

    @Test
    void testDeleteSeller_NotFound() {
        when(sellersRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = sellersService.deleteSeller(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Seller with id: 1 not found", error.getMessage());
    }

    @Test
    void testUpdateSellerInfo_Success() {
        SellerReq sellerReq = new SellerReq();
        sellerReq.setName("Sanya");
        sellerReq.setContactInfo("sanya@gmail.com");

        when(sellersRepository.findById(1L)).thenReturn(Optional.of(testUpdSeller));

        ResponseEntity<?> response = sellersService.updateSellerInfo(1L, sellerReq);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SellerRes sellerRes = (SellerRes) response.getBody();
        assertNotNull(sellerRes);
        assertEquals(testUpdSeller.getName(), sellerRes.getName());
    }

    @Test
    void testUpdateSellerInfo_NotFound() {
        SellerReq sellerReq = new SellerReq();
        sellerReq.setName("Sanya");
        sellerReq.setContactInfo("sanya@gmail.com");

        when(sellersRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = sellersService.updateSellerInfo(2L, sellerReq);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Seller with id: 2 not found", error.getMessage());
    }
}
