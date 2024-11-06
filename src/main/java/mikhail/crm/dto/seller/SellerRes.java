package mikhail.crm.dto.seller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import mikhail.crm.entity.Seller;

import java.time.LocalDateTime;

@Data
public class SellerRes {
    private long id;
    private String name;

    @JsonProperty("contact_info")
    private String contactInfo;

    @JsonProperty("registration_date")
    private LocalDateTime registrationDate;

    public SellerRes(Seller s) {
        id = s.getId();
        name = s.getName();
        contactInfo = s.getContactInfo();
        registrationDate = s.getRegistrationDate();
    }
}
