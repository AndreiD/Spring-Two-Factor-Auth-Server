package springtemplate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import springtemplate.forms.LoginForm;
import springtemplate.services.NotificationService;
import springtemplate.services.UserService;

import javax.validation.Valid;


@Controller
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notifyService;


    @RequestMapping("/users/login")
    public String login(LoginForm loginForm) {
        return "/users/login";
    }


    public String loginPage(@Valid LoginForm loginForm, BindingResult bindingResult) {
        log.info("Biding result is " + bindingResult.toString());
        if (bindingResult.hasErrors()) {
            notifyService.error_notification("Please fill the form correctly");
            return "users/login";
        }

        if (!userService.authenticate(
                loginForm.getEmail(), loginForm.getPassword())) {
            notifyService.error_notification("Invalid login");
            return "users/login";
        }

        notifyService.info_notification("Login successful");
        return "redirect:/";
    }

    @RequestMapping("/users/logout")
    public String logout() {
        notifyService.info_notification("Bye");
        return "redirect:/";
    }


}