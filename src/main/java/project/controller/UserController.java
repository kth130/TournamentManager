package project.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import project.persistence.entities.Role;
import project.persistence.entities.Tournament;
import project.persistence.entities.User;
import project.service.Interfaces.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private final String path = "user/";

    private IUserService userService;
    private IRoleService roleService;
    private IAuthenticationService authenticationService;
    private Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    public UserController(IUserService userService, IRoleService roleService, IAuthenticationService authenticationService){

        this.roleService = roleService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    /**
     * Set up the login view, the login itself is handled by spring security
     * @param model
     * @param error
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model,
                           @RequestParam(name = "error", required = false, defaultValue = "false")String error){
        ArrayList<String> errors = new ArrayList<>();
        if(Boolean.valueOf(error)) errors.add("Login unsuccessful, try again");
        model.addAttribute("title", "Login");
        model.addAttribute("target", "/login");
        model.addAttribute("errors", errors);

        return path + "Login_Signup";
    }

    /**
     * Set up the Sign up view
     * @param model
     * @return
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupGet(Model model){
        model.addAttribute("title", "Signup");
        model.addAttribute("target", "/signup");
        return path + "Login_Signup";
    }

    /**
     * Signs up a user, including some validation
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(Model model,
                             @RequestParam(value = "username") String username,
                             @RequestParam(value = "password") String password){
        ArrayList<String> errors = new ArrayList<>();

        // Lightweight validation
        if(userService.findByUsername(username.toUpperCase()) != null){
            errors.add("Username already in use.");
        }
        if(username.length() < 5){
            errors.add("Username has to be at least 5 letters.");
        }
        if(password.length() < 5){
            errors.add("Password has to be at least 5 letters.");
        }
        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
            model.addAttribute("title","Signup");
            model.addAttribute("target", "/signup");

            return path + "Login_Signup";
        }

        User user = new User(username.toUpperCase(), userService.hashPW(password));

        // Setup up default ROLE as user
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(user, "ROLE_USER"));

        user.setRoles(roles);
        userService.save(user);

        for(Role r : roles)
            roleService.save(r);

        logger.info("User created: " + user.getUsername());
        return "redirect:/login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profileGet(Model model){
        User user = userService.findByUsername(authenticationService.getUsername());
        Set<Tournament> tournaments = user.getTournaments();

        // Setup authentication
        if(authenticationService.isAuthenticated()){
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("username", authenticationService.getUsername());
            model.addAttribute("tournaments", tournaments);
        }

        return path + "Profile";
    }

}
