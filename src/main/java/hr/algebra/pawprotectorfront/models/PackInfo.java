package hr.algebra.pawprotectorfront.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PackInfo {
    private String sellerContectInfo;
    private String dog;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime birthDate;
    private int maleCount;
    private int fMaleCount;
    private String description;
    private double price;
}
