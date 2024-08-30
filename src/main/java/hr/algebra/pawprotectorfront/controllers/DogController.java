package hr.algebra.pawprotectorfront.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pawprotectorfront.models.Dog;
import hr.algebra.pawprotectorfront.services.HksApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


@Controller
public class DogController {
    Logger logger = LoggerFactory.getLogger(DogController.class);

    private final HksApiService hksApiService;

    public DogController(HksApiService hksApiService) {
        this.hksApiService = hksApiService;
    }
    @GetMapping("/success")
    public String success() {
        return "success";
    }
    @GetMapping("/")
    public String commonEndpoint(Model model) {
        String token = hksApiService.getToken();
        List<Dog> dogs = null;
        try {
            dogs = hksApiService.getAllDogs(token);
        } catch (JsonProcessingException e) {
            logger.error("Error occurred while trying to getDogs "+ e);
            return "error";
        }
        model.addAttribute("dogs", dogs);
        return "dogs";
    }

    @GetMapping("about")
    public String aboutUs(Model model){
        return "about";
    }


}
