package springtemplate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springtemplate.forms.RegistrationForm;
import springtemplate.services.NotificationService;
import springtemplate.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notifyService;


    @GetMapping("/users/register")
    public String showRegistrationForm(RegistrationForm registrationForm) {
        return "/users/register";
    }


    @PostMapping("/users/register")
    public String checkRegisterInfo(@Valid RegistrationForm registrationForm, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            notifyService.error_notification("Ops! Seems that you missed something...");
            return "users/register";
        }

        String last_ip = request.getRemoteAddr().toString();
        String fullName = registrationForm.getFullName();
        String email = registrationForm.getEmail();
        String password = registrationForm.getPassword();

        if (!userService.register(fullName, email, password, last_ip)) {
            notifyService.error_notification("Invalid registration. Please contact us");
            return "users/register";
        }

        notifyService.info_notification("Registration successful");

        redirectAttributes.addFlashAttribute("user_email", registrationForm.getEmail());
        return "redirect:/users/displaykey";
    }


    //one time show the key...
    @GetMapping("/users/displaykey")
    public String showtheKey(Model model) {

        if (!model.asMap().containsKey("user_email")) {
            log.error("map does not contain user_email parameter");
            return "redirect:/error";
        }

        String user_email = (String) model.asMap().get("user_email");
        try {
            String unique_key = userService.getUniqueKey(user_email);
            model.addAttribute("unique_key", unique_key);
            return "/users/displaykey";
        } catch (Exception ex) {
            log.error("could not get the unique_key");
            return "redirect:/error";
        }
    }
}