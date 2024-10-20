package mikhail.crm.api.controller;

import mikhail.crm.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/top-seller")
    public ResponseEntity<?> getTopSeller(
            @RequestParam("start_time") LocalDateTime startTime,
            @RequestParam("end_time") LocalDateTime endTime) {
        return analyticsService.getTopSellerByPeriod(startTime, endTime);
    }

    @GetMapping("/looser-sellers")
    public ResponseEntity<?> getLoosers(
            @RequestParam("start_time") LocalDateTime startTime,
            @RequestParam("end_time") LocalDateTime endTime,
            @RequestParam("amount") long amount) {
        return analyticsService.getLooserSellersByPeriod(startTime, endTime, amount);
    }
}
