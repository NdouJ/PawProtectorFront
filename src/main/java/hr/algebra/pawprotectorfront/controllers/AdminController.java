package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    @GetMapping("/admin/newHksPdf")
    public String newHksPdf(Model model) {
        model.addAttribute("url", new String());
        return "newHksPdf";
    }

    @PostMapping("/admin/newHksPdf")
    public String newHksPdf(@ModelAttribute String url, Model model) {
        model.addAttribute("url", new String());
        try {
            String token = hksApiService.getToken();
            hksApiService.postNewHksBreeds(token, url);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to post hks breeds. Please try again.");
        }

        return "success";
    }

    @GetMapping("/admin/getSellers")
    public String getAdminSellers(Model model) {
        List<Seller> sellers = new ArrayList<>();

        try {
            sellers=hksApiService.getAllSellers(hksApiService.getToken());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("sellers", sellers);
        return "adminCrudSellers";
    }


    @GetMapping("/admin/updateSeller")
    public String updateSeller(@RequestParam("id") Integer idSeller, Model model) {
        Seller updateSeller = new Seller();
        try {
            updateSeller  = hksApiService.getSellerByID(hksApiService.getToken(),idSeller);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("updateSeller", updateSeller);
        return "updateSeller";
    }
    @PostMapping("/admin/updateSeller")
    public String processUpdateSeller(@ModelAttribute("updateSeller") Seller updateSeller) {
        try {
            hksApiService.updateSeller(hksApiService.getToken(), updateSeller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/admin/success";
    }
    // Delete Seller
    @GetMapping("/admin/deleteSeller")
    public String deleteSeller(@RequestParam("id") Integer idSeller, Model model) {
        try {
            hksApiService.deleteSellerById(hksApiService.getToken(), idSeller);
            return "success";
        } catch (Exception e) {
            return "error";
        }
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
