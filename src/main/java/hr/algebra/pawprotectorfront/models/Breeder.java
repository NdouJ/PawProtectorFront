package hr.algebra.pawprotectorfront.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Breeder {

    private Integer idBreeder;
    private String oib;
    private String breederName;
    private String breederContact;
    private String breederInfo;

}

