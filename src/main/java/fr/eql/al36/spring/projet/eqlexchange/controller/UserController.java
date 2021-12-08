package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("sessionUser")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/dashboard")
    public String displayDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String connectedUserEmail = userDetails.getUsername();
        User connectedUser = userService.findUserByEmail(connectedUserEmail);
        if(connectedUser == null){
            return "user/non-existent";
        }
        model.addAttribute("sessionUser", connectedUser);
        return "user/dashboard";
    }

    @GetMapping("access-denied")
    public String accessDenied(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String connectedUserEmail = userDetails.getUsername();
        User connectedUser = userService.findUserByEmail(connectedUserEmail);
        model.addAttribute("sessionUser", connectedUser);
        return "access-denied";
    }

}
