package de.sieberss.backend.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String getMe(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping()
    public String login(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/register")
    public void registerAdmin(@RequestBody String password){
        loginService.addNewAdminUser(password);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        //Session beenden!
        session.invalidate();
        //SecurityContext löschen
        SecurityContextHolder.clearContext();
    }

}
