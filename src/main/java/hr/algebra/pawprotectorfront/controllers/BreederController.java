package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pawprotectorfront.models.Breeder;
import hr.algebra.pawprotectorfront.models.Dog;
import hr.algebra.pawprotectorfront.models.UserReview;
import hr.algebra.pawprotectorfront.services.HksApiService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class BreederController {
    Logger logger = LoggerFactory.getLogger(BreederController.class);
    private final HksApiService hksApiService;
    public BreederController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @GetMapping("/breederOfDog")
    public String getBreedersOfDog(@RequestParam("dogName") String dogName, Model model) {
        try {
            String token = hksApiService.getToken();
            List<Breeder> breeders = hksApiService.getBreedersOfDog(token, dogName);
            model.addAttribute("breeders", breeders);
            return "breeders";
        } catch (Exception e) {
            logger.error("Error occurred while trying to breeders "+ e);
            return "error";
        }
    }

    @GetMapping("/breederDetails/{id}")
    public String getBreederDetails(@PathVariable("id") Integer breederId, Model model, HttpSession session) {
        session.setAttribute("breederId", breederId);
        List<UserReview> userReviews = new ArrayList<>();
        try {
          userReviews=  hksApiService.getUserReviewsByBreederId(hksApiService.getToken(), breederId);
        } catch (JsonProcessingException e) {
            logger.error("Error occurred while trying to breeder "+ e);
            return "error";
        }
        model.addAttribute("userReviews", userReviews);
        return "breederDetails";
    }

}
