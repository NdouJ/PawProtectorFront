package hr.algebra.pawprotectorfront.services;


import hr.algebra.pawprotectorfront.models.PackInfo;
import hr.algebra.pawprotectorfront.models.Seller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Service
public class HksApiService {
    @Value("${api.login.url}")
    private String loginUrl;

    @Value("${api.dogs.url}")
    private String dogsUrl;

    @Value("${api.breedersOfDog.url}")
    private String breedersOfDog;

    @Value ("${api.postSeller.url}")
    private String postSeller;

    @Value("${api.getBreeders.url}")
    private String getBreeders;

    @Value("${api.checkUser.url}")
    private String checkUser;

    @Value("${api.packInfo.url}")
    private String postPackInfo;

    private final RestTemplate restTemplate;
    private String token;
    public HksApiService(){
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
    public String getToken() {

        if (token != null && !token.isEmpty()) {
            return token;
        }
        ResponseEntity<String> response = restTemplate.getForEntity(loginUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String token = response.getBody();

            if (token != null && !token.isEmpty()) {
                // Remove leading and trailing quotes, if present
                token = token.replaceAll("^\"|\"$", "");
                return token;
            } else {
                throw new RuntimeException("Token is empty or null");
            }
        } else {
            throw new RuntimeException("Failed to get token: " + response.getStatusCode());
        }
    }


    public String getAllDogs(String token) {
        // Set up headers with the Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(dogsUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get dogs: " + response.getStatusCode());
        }
    }

    //Breeder
    public String getBreedersOfDog(String token, String dogName) {
        String url = String.format("%s?dog=%s", breedersOfDog, dogName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get breeders of dog: " + response.getStatusCode());
        }
    }

    public String getAllBreeders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(getBreeders, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get Breeders: " + response.getStatusCode());
        }
    }
//seller
public String postSeller(String token, Seller seller) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    HttpEntity<Seller> entity = new HttpEntity<>(seller, headers);
    seller.setIdSeller(1);

    ResponseEntity<String> response = restTemplate.exchange(postSeller, HttpMethod.POST, entity, String.class);
    if (response.getStatusCode() == HttpStatus.OK) {
        return response.getBody();
    } else {
        throw new RuntimeException("Failed to post Seller " + response.getStatusCode());
    }
}

    public String checkSeller(Seller seller) {
        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + token);

        hr.algebra.pawprotectorfront.models.User user = new hr.algebra.pawprotectorfront.models.User();
        user.setId(1);
        user.setUsername(seller.getContactInfo());
        user.setPasswordHash(seller.getTempPassword());
        user.setRoleId(2);
        HttpEntity< hr.algebra.pawprotectorfront.models.User> entity = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(checkUser, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return "OK";
        }
        else if(response.getStatusCode()==HttpStatus.UNAUTHORIZED){
            return "Wrong credentials";
        }
        else {
            throw new RuntimeException("Failed to check Seller " + response.getStatusCode());
        }
    }


    public String savePackInfo(String token, PackInfo packInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        packInfo.setDog("dog");
        packInfo.setMaleCount(1);
        packInfo.setFMaleCount(1);
        packInfo.setBirthDate(OffsetDateTime.now());
        HttpEntity<PackInfo> entity = new HttpEntity<>(packInfo, headers);
        ResponseEntity<String> response = restTemplate.exchange(postPackInfo, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to post Seller " + response.getStatusCode());
        }
    }
}
