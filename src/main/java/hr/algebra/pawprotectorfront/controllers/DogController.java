package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pawprotectorfront.models.Dog;
import hr.algebra.pawprotectorfront.services.HksApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

import java.util.List;

@Controller
public class DogController {

    private final HksApiService hksApiService;

    public DogController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @GetMapping("/")
    public String commonEndpoint(Model model) {
        String token = hksApiService.getToken();
        String jsonResponse = hksApiService.getAllDogs(token);

        // Parse JSON response to List of Dog objects
        List<Dog> dogs = parseDogs(jsonResponse);

        model.addAttribute("dogs", dogs);
        return "dogs";
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
