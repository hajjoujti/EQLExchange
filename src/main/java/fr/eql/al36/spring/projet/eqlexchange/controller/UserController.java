package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.service.UserService;
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
    public String displayDashboard(Model model){
        User connectedUser = userService.findUserById(4);
        if(connectedUser == null){
            return "user/non-existant";
        }
        model.addAttribute("sessionUser", connectedUser);
        return "user/dashboard";
    }

}
