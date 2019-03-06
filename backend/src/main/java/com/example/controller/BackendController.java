package com.example.controller;
import com.example.domain.User;
import com.example.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.security.Principal;

@RestController()
@RequestMapping(path = "/api")
public class BackendController {

    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addNewUser (@RequestParam Map<String, String> requestParams)  {

        String firebaseID = "";
        String userName = "";
        String userEmail = "";
        String password = "";
        String signInMethod = "";
        String message = "";

        if (requestParams.containsKey("uid")) {
            try {
                String uid = requestParams.get("uid");
                FirebaseToken dataObjectFromToken = FirebaseAuth.getInstance().verifyIdToken(uid);
                firebaseID = dataObjectFromToken.getUid();
                userName = dataObjectFromToken.getName();
                userEmail = dataObjectFromToken.getEmail();
                signInMethod = requestParams.get("method");
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        }
        else {
            userEmail = requestParams.get("email");
            password = requestParams.get("password");
        }

        User user = new User();
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPassword(password);
        if (signInMethod.equals("fb")) {
            user.setFBID(firebaseID);
        }
        else if (signInMethod.equals("gl")) {
            user.setGoogleID(firebaseID);
        }

        User ifUserInDB = userRepository.findByEmail(userEmail);
        if (ifUserInDB == null) {
            userRepository.save(user);
            message = "Saved";
        }
        else {
            message = "Exists";
        }
        LOG.info(user + " successfully saved into DB");
        return message;
    }

    @GetMapping(path="/user/{id}")
    public @ResponseBody
    String getUserById(@PathVariable("id") Integer id) {
        User userFromDB = userRepository.getUserById(id);
        return userFromDB.getName();
    }

    @GetMapping(path="/user/info")
    public @ResponseBody
    String info(Principal principal) {
        return "Current user: " + principal.getName();
    }
}
