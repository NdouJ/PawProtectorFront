package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pawprotectorfront.models.Breeder;
import hr.algebra.pawprotectorfront.models.Dog;
import hr.algebra.pawprotectorfront.services.HksApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class BreederController {
    private final HksApiService hksApiService;

    public BreederController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @GetMapping("/breederOfDog")
    public String getBreedersOfDog(@RequestParam("dogName") String dogName, Model model) {
        try {
            String token = hksApiService.getToken();
            String jsonResponse = hksApiService.getBreedersOfDog(token, dogName);

            // Assuming parseBreeders parses JSON correctly
            List<Breeder> breeders = parseBreeders(jsonResponse);

            model.addAttribute("breeders", breeders);
            return "breeders";
        } catch (Exception e) {
            e.printStackTrace();  // Log the error
            return "error";  // Return an error view if something goes wrong
        }
    }

    private List<Breeder> parseBreeders(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Breeder>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Dog> parseDogs(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Dog>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
