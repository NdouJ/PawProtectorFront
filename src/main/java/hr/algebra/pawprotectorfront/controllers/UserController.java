package hr.algebra.pawprotectorfront.controllers;

import hr.algebra.pawprotectorfront.models.Donation;
import hr.algebra.pawprotectorfront.models.UserReview;
import hr.algebra.pawprotectorfront.services.HksApiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
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
        //prvo moramo postati usera, ako nije
         String username = GetOath2UserName();
         hksApiService.postOath2User(username, hksApiService.getToken());

        //uzeti userID
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

        return "success";
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
}
