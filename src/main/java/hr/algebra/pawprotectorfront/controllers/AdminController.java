package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pawprotectorfront.models.Breeder;
import hr.algebra.pawprotectorfront.models.Seller;
import hr.algebra.pawprotectorfront.services.EmailService;
import hr.algebra.pawprotectorfront.services.HksApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    private final HksApiService hksApiService;
    private final EmailService emailService;

    public AdminController(HksApiService hksApiService, EmailService emailService) {
        this.hksApiService = hksApiService;
        this.emailService = emailService;
    }

    @GetMapping("/admin/Dashboard")
    public String GetAdminDashboard(){

        return "adminDashboard";
    }

    @GetMapping("/admin/addSeller")
    public String showAddSellerForm(Model model) {
        model.addAttribute("seller", new Seller());
        try {
            String token = hksApiService.getToken();
            String breedersJsonResponse = hksApiService.getAllBreeders(token);
            List<Breeder> breeders = parseBreeders(breedersJsonResponse);
            model.addAttribute("breeders", breeders);


        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Oops something went wrong");
        }

        return "addSellerForm";
    }

    @PostMapping("/admin/addSeller")
    public String addSeller(@ModelAttribute Seller seller, Model model) {

        try {
            String token = hksApiService.getToken();

            hksApiService.postSeller(token, seller);
            emailService.sendEmail(seller.getContactInfo(), seller.getTempPassword());
            model.addAttribute("seller", seller);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to add seller. Please try again.");
        }

        return "success";
    }



    public List<Breeder> parseBreeders(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Breeder>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
