package hr.algebra.pawprotectorfront.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {


    @GetMapping("/admin/Dashboard")
    public String GetAdminDashboard(){


        return "adminDashboard";
    }
}
