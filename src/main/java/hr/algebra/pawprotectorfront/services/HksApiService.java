package hr.algebra.pawprotectorfront.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pawprotectorfront.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


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

    @Value("${api.Key}")
    private String apiKey;

    @Value("${api.postUser.url}")
    private String postUser;
    @Value("${api.postReview.url}")
    private String postReview;
    @Value("${api.getOath2User.url}")
    private String getOath2User;
    @Value("${api.donation.url}")
    private String postDonation;
    @Value("${api.getUserReviewsByBreederId.url}")
    private String getUserReviewBsByUserId;
    @Value("${api.getAllSellers.url}")
    private String getAllSellers;
    @Value("${api.getpackInfo.url}")
    private String getPackInfo;
    @Value("${api.postNewBreeders.url}")
    private String postHksBreeders;

    @Value("${api.deleteSeller.url}")
    private String deleteSeller;
    @Value("${api.updateSeller.url}")
    private String updateSeller;

    @Value("${api.getSellerById.url}")
    private String getSellerById;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private String token;
    public HksApiService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
    public String getToken() {

        if (token != null && !token.isEmpty()) {
            return token;
        }
        String url=loginUrl+"?apiKey="+apiKey;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

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


    public List<Dog> getAllDogs(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(dogsUrl, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Dog>>() {});

        } else {
            throw new RuntimeException("Failed to get dogs: " + response.getStatusCode());
        }
    }

    //Breeder
    public List<Breeder> getBreedersOfDog(String token, String dogName) throws JsonProcessingException {
        String url = String.format("%s?dog=%s", breedersOfDog, dogName);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Breeder>>() {});

        } else {
            throw new RuntimeException("Failed to get breeders of dog: " + response.getStatusCode());
        }
    }

    public List<Breeder> getAllBreeders(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(getBreeders, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Breeder>>() {});
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
       // headers.set("Authorization", "Bearer " + token);

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
        packInfo.setBirthDate(LocalDateTime.now());
        HttpEntity<PackInfo> entity = new HttpEntity<>(packInfo, headers);
        ResponseEntity<String> response = restTemplate.exchange(postPackInfo, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to post Pack " + response.getStatusCode());
        }
    }

    public void postOath2User(String username, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        hr.algebra.pawprotectorfront.models.User oathUser = new User();
        oathUser.setRoleId(3);
        oathUser.setUsername(username);
        oathUser.setPasswordHash("3");
        oathUser.setId(1);

        HttpEntity<User> entity = new HttpEntity<>(oathUser, headers);
        headers.set("Content-Type", "application/json");
        ResponseEntity<String> response = restTemplate.exchange(postUser, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {

            throw new RuntimeException("Failed to post User " + response.getStatusCode());
        }

    }


    public Integer getOathUserId(String username, String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

String url = getOath2User+"?name="+username;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ParseUser(response.getBody());
        } else {
            throw new RuntimeException("Failed to userID " + response.getStatusCode());
        }
    }

    private Integer ParseUser(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(body, User.class);
            // Assuming you want to return the user's ID after parsing
            return user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
            return null;
        }
    }

    public void postUserReview(UserReview userReview, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserReview> entity = new HttpEntity<>(userReview, headers);
        ResponseEntity<String> response = restTemplate.exchange(postReview, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() != HttpStatus.CREATED) {

            throw new RuntimeException("Failed to post userReview " + response.getStatusCode());
        }
    }

    public void postDonation(String token, Donation donation) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        donation.setDonationTime(OffsetDateTime.now());
        donation.setIdDonation(1);
        HttpEntity<Donation> entity = new HttpEntity<>(donation, headers);
        headers.set("Content-Type", "application/json");
        ResponseEntity<String> response = restTemplate.exchange(postDonation, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() != HttpStatus.CREATED) {

            throw new RuntimeException("Failed to post Donation " + response.getStatusCode());
        }
    }

    public List<UserReview> getUserReviewsByBreederId(String token, Integer breederId) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        String url = getUserReviewBsByUserId +"?id="+breederId;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<UserReview>>() {});

        } else {
            throw new RuntimeException("Failed to get Breeders: " + response.getStatusCode());
        }
    }

    public List<Seller> getAllSellers(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(getAllSellers, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<List<Seller>>() {});

        } else {
            throw new RuntimeException("Failed to get Sellers: " + response.getStatusCode());
        }
    }
    public String deleteSellerById(String token, Integer id)  {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String deleteUrl= deleteSeller +"/"+id;
        ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return "deleted";
        } else {
            throw new RuntimeException("Failed to get Sellers: " + response.getStatusCode());
        }
    }

    public Seller getSellerByID(String token, Integer Id) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        String url = getSellerById +Id;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<Seller>() {});

        } else {
            throw new RuntimeException("Failed to get Breeders: " + response.getStatusCode());
        }
    }

    public String updateSeller(String token, Seller seller) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Seller> entity = new HttpEntity<>(seller, headers);

        ResponseEntity<String> response = restTemplate.exchange(updateSeller, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to post Seller " + response.getStatusCode());
        }
    }
    public PackInfo getPackInfo(String token, String breederContact) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        breederContact = breederContact.replace("%40", "@");
        String url = getPackInfo+breederContact;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), new TypeReference<PackInfo>() {});

        } else {
            throw new RuntimeException("Failed to get Sellers: " + response.getStatusCode());
        }
    }

    public void postNewHksBreeds(String token, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String fullUrl = postHksBreeders +"?url="+url;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, entity, String.class);
    }

    }

