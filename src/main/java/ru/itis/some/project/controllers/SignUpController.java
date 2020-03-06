package ru.itis.some.project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.some.project.dto.SignUpDto;
import ru.itis.some.project.services.ConfirmSignUpService;
import ru.itis.some.project.services.SignUpService;
import ru.itis.some.project.util.exceptions.ServiceException;

@Controller
@AllArgsConstructor
@RequestMapping("/signUp")
public class SignUpController {
    private final SignUpService signUpService;
    private final ConfirmSignUpService confirmSignUpService;

    @GetMapping
    public ModelAndView getSignUpPage(@RequestParam(required = false) String error) {
        return new ModelAndView("sign_up", "error", error);
    }


    @PostMapping
    public ModelAndView signUp(SignUpDto dto) {
        try {
            signUpService.signUp(dto);
            return new ModelAndView("sign_up_confirm", "email", dto.getEmail());
        } catch (ServiceException e) {
            return getSignUpPage(e.getMessage());
        }
    }

    @GetMapping("/confirm/{token}")
    public String confirm(@PathVariable String token) {
        try {
            if (confirmSignUpService.confirm(token)) {
                return "sign_up_confirmed";
            } else {
                return "redirect:/";
            }
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
