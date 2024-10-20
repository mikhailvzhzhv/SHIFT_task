package mikhail.crm;

import mikhail.crm.api.error.RequestError;
import mikhail.crm.dto.seller.SellerRes;
import mikhail.crm.entity.Seller;
import mikhail.crm.repository.SellersRepository;
import mikhail.crm.repository.TransactionsRepository;
import mikhail.crm.service.AnalyticsService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@SpringBootTest
class AnalyticsServiceTests {

    @Autowired
    private AnalyticsService analyticsService;

    @MockBean
    private TransactionsRepository transactionsRepository;

    @MockBean
    private SellersRepository sellersRepository;

    private Seller testSeller;

    @BeforeEach
    void init() {
        testSeller = new Seller();
        testSeller.setId(1L);
        testSeller.setName("Mikhail");
        testSeller.setContactInfo("mvzhzhv@gmail.com");
        testSeller.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void testGetTopSellerByPeriod_Success() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        when(transactionsRepository.getTopSellerByPeriod(startTime, endTime)).thenReturn(Optional.of(1L));
        when(sellersRepository.findById(1L)).thenReturn(Optional.of(testSeller));

        ResponseEntity<?> response = analyticsService.getTopSellerByPeriod(startTime, endTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        SellerRes sellerRes = (SellerRes) response.getBody();
        assertNotNull(sellerRes);
        assertEquals(testSeller.getName(), sellerRes.getName());
    }

    @Test
    void testGetTopSellerByPeriod_NoSellerInTransactions() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        when(transactionsRepository.getTopSellerByPeriod(startTime, endTime)).thenReturn(Optional.empty());

        ResponseEntity<?> response = analyticsService.getTopSellerByPeriod(startTime, endTime);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Cannot find top seller with params", error.getMessage());
    }

    @Test
    void testGetTopSellerByPeriod_SellerNotFound() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        when(transactionsRepository.getTopSellerByPeriod(startTime, endTime)).thenReturn(Optional.of(1L));
        when(sellersRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = analyticsService.getTopSellerByPeriod(startTime, endTime);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        RequestError error = (RequestError) response.getBody();
        assertNotNull(error);
        assertEquals("Top seller not found", error.getMessage());
    }

    @Test
    void testGetLooserSellersByPeriod_Success() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        long amount = 10000;

        List<Long> sellerIds = new ArrayList<>();
        sellerIds.add(1L);
        List<Seller> sellers = new ArrayList<>();
        sellers.add(testSeller);

        when(transactionsRepository.getTopSellerLowerAmountByPeriod(startTime, endTime, amount)).thenReturn(sellerIds);
        when(sellersRepository.findAllById(sellerIds)).thenReturn(sellers);

        ResponseEntity<?> response = analyticsService.getLooserSellersByPeriod(startTime, endTime, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<SellerRes> sellerResList = (List<SellerRes>) response.getBody();
        assertNotNull(sellerResList);
        assertEquals(1, sellerResList.size());
        assertEquals(testSeller.getName(), sellerResList.get(0).getName());
    }

    @Test
    void testGetLooserSellersByPeriod_NoSellersFound() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        long amount = 10000;

        when(transactionsRepository.getTopSellerLowerAmountByPeriod(startTime, endTime, amount)).thenReturn(new ArrayList<>());
        when(sellersRepository.findAllById(new ArrayList<>())).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = analyticsService.getLooserSellersByPeriod(startTime, endTime, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<SellerRes> sellerResList = (List<SellerRes>) response.getBody();
        assertNotNull(sellerResList);
        assertTrue(sellerResList.isEmpty());
    }
}
