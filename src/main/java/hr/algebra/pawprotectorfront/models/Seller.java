package hr.algebra.pawprotectorfront.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seller {

    private Integer idSeller;
    private String breederName;
    private String contactInfo;
    private String oib;
    private String tempPassword=generateTempPassword();


    public static String generateTempPassword() {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final int passwordLength = 4;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

}
