package mikhail.crm.dto.seller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SellerReq {
    private String name;

    @JsonProperty("contact_info")
    private String contactInfo;

    private LocalDateTime registrationDate;
}
