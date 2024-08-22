package hr.algebra.pawprotectorfront.controllers;

import hr.algebra.pawprotectorfront.models.PackInfo;
import hr.algebra.pawprotectorfront.services.HksApiService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SellerController {
    private final HksApiService hksApiService;

    public SellerController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }

    @GetMapping("/breeder/seller/publishDog")
    public String showAddPackInfoForm(Model model) {
        model.addAttribute("packInfo", new PackInfo());
        return "publishDogForm";
    }

    @PostMapping("/breeder/seller/publishDog")
    public String addPackInfo(@ModelAttribute PackInfo packInfo, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String sellerContactInfo = userDetails.getUsername();
            packInfo.setSellerContectInfo(sellerContactInfo);
        }
        String token = hksApiService.getToken();
        hksApiService.savePackInfo(token, packInfo);
        model.addAttribute("message", "Pack information published successfully!");
        return "success";
    }
}
