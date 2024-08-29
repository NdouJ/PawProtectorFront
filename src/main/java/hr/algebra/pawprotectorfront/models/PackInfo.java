package hr.algebra.pawprotectorfront.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PackInfo {
    private String sellerContactInfo;
    private String dog;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;
    private Integer maleCount;
    private Integer fMaleCount;
    private String description;
    private BigDecimal price;
}