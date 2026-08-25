package space.yogeshwarank.studysync.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller exposes a "/" endpoint.
 * It is to check for the working of the service.
 */
@RestController
@RequestMapping("")
public class HomeController {

    @GetMapping("")
    public String home(){
        return "Welcome to StudySync !!!";
    }
}
