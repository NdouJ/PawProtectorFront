package hr.algebra.pawprotectorfront.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class HksApiService {
    @Value("${api.login.url}")
    private String loginUrl;

    @Value("${api.dogs.url}")
    private String dogsUrl;

    @Value("${api.breedersOfDog.url}")
    private String breedersOfDog;

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

}
