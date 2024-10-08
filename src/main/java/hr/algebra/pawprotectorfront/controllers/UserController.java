package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import hr.algebra.pawprotectorfront.models.Donation;
import hr.algebra.pawprotectorfront.models.PackInfo;
import hr.algebra.pawprotectorfront.models.Seller;
import hr.algebra.pawprotectorfront.models.UserReview;
import hr.algebra.pawprotectorfront.services.HksApiService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final HksApiService hksApiService;

    public UserController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @GetMapping("/user/addReview")
    public String addReview(Model model) {
        model.addAttribute("userReview", new UserReview());
        return "addReview";
    }

    @PostMapping("/user/addReview")
    public String addReview(@ModelAttribute UserReview userReview, Model model, HttpSession session) {

        try{
            String username = GetOath2UserName();
            hksApiService.postOath2User(username, hksApiService.getToken());

            userReview.setUserId(hksApiService.getOathUserId(username, hksApiService.getToken()));

            // Retrieve the breederId from the session
            Integer breederId = (Integer) session.getAttribute("breederId");
            if (breederId != null) {
                userReview.setBreederId(breederId);
                session.removeAttribute("breederId");
            } else {
                model.addAttribute("error", "Breeder ID not found in session");
            }
            // postatiUserReview
            userReview.setIdUserReview(1);
            hksApiService.postUserReview(userReview, hksApiService.getToken());
        }
        catch (Error e) {
            logger.error("Error occurred while trying to /user/addReview "+ e);
        return "error";
        }
        return "redirect:/success";
    }

    private String GetOath2UserName() {
        String username = "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oAuth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();
            username = oAuth2User.getAttribute("login"); // GitHub login (username)
        }

        return username;
    }


    @GetMapping("/user/donation")
    public String addFonation(Model model) {
        model.addAttribute("donation", new Donation());
        return "donation";
    }

    @PostMapping ("/user/donation")
    public String addFonation(Donation donation, Model model) {
        model.addAttribute("donation", new Donation());
        String username= GetOath2UserName();
        hksApiService.postOath2User(username, hksApiService.getToken());
        donation.setUserId(hksApiService.getOathUserId(username, hksApiService.getToken()));
        double amount = donation.getAmount();
        hksApiService.postDonation(hksApiService.getToken(), donation);
        return "redirect:/paypal/donate?amount=" + amount;
    }

    @GetMapping("/user/getSellers")
    public String getSellers(Model model) {
        List<Seller> sellers = new ArrayList<>();

        try {
            sellers=hksApiService.getAllSellers(hksApiService.getToken());
        } catch (JsonProcessingException e) {
            logger.error("Error occurred while trying to /user/addReview "+ e);
            return "error";
        }
        model.addAttribute("sellers", sellers);
        return "getSellers";
    }
    @GetMapping("/user/getSellersPack")
    public String getSellersPack(@RequestParam("contactInfo") String contactInfo, Model model) {
        PackInfo packInfo = new PackInfo();
        try {
            packInfo = hksApiService.getPackInfo(hksApiService.getToken(), contactInfo);
        } catch (JsonProcessingException e) {
            logger.error("Error occurred while trying to /user/addReview "+ e);
            return "error";
        }
        model.addAttribute("packInfo", packInfo);
        return "sellersPack";
    }
}
