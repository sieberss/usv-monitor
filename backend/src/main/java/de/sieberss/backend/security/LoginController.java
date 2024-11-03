package de.sieberss.backend.security;

import de.sieberss.backend.model.CredentialsWithoutEncryption;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
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
        System.out.println("login");
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/register")
    public void register(@RequestBody CredentialsWithoutEncryption credentials){
        loginService.register(credentials);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        //Session beenden!
        session.invalidate();
        //SecurityContext löschen
        SecurityContextHolder.clearContext();
    }

}
