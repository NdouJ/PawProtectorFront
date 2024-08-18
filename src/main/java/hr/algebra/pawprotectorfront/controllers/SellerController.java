package hr.algebra.pawprotectorfront.controllers;

import hr.algebra.pawprotectorfront.models.Dog;
import hr.algebra.pawprotectorfront.services.HksApiService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class SellerController {

    private final HksApiService hksApiService;

    public SellerController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    /*
    @GetMapping("/login")
    public String commonEndpoint(Model model) {
        String jsonResponse = hksApiService.getAllDogs(token);

        // Parse JSON response to List of Dog objects
        List<Dog> dogs = parseDogs(jsonResponse);

        model.addAttribute("dogs", dogs);
        return "dogs";
    }

     */
}
