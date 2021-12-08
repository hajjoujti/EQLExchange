package fr.eql.al36.spring.projet.eqlexchange.controller;

import fr.eql.al36.spring.projet.eqlexchange.domain.Asset;
import fr.eql.al36.spring.projet.eqlexchange.domain.User;
import fr.eql.al36.spring.projet.eqlexchange.service.AssetService;
import fr.eql.al36.spring.projet.eqlexchange.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("sessionUser")
public class UserController {

    private final UserService userService;
    private final AssetService assetService;


    public UserController(UserService userService, AssetService assetService) {
        this.userService = userService;
        this.assetService = assetService;
    }

    @GetMapping("user/dashboard")
    public String displayDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String connectedUserEmail = userDetails.getUsername();
        User connectedUser = userService.findUserByEmail(connectedUserEmail);
        List<Asset> assets = assetService.getAllByUser(connectedUser);
        if(connectedUser == null){
            return "user/non-existent";
        }
        model.addAttribute("sessionUser", connectedUser);
        model.addAttribute("assets", assets);
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
