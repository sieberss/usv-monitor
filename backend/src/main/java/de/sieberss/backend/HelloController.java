package de.sieberss.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usv-monitor")
public class HelloController {
    @GetMapping
    public String hello() {
        return "Hello World!";
    }
}
