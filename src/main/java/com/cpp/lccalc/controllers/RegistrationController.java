package com.cpp.lccalc.controllers;

import com.cpp.lccalc.models.Role;
import com.cpp.lccalc.models.User;
import com.cpp.lccalc.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    /*@Autowired
    private UserService userService;*/

    @Autowired
    UserRepository userRepository;



    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        userForm.setActive(true);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        User userFromDb = userRepository.findByUsername(userForm.getUsername());

        if (userFromDb!=null){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        userForm.setPassword(encoder.encode(userForm.getPassword()));
        userForm.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));

        userRepository.save(userForm);

        return "redirect:/projects";
    }
}