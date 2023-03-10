package com.example.security.web;

import com.example.security.auth.AuthenticationRequest;
import com.example.security.auth.AuthenticationResponse;
import com.example.security.auth.AuthenticationService;
import com.example.security.responseBodyModel.UserData;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@Api(tags = "User login Controller", description = "Operations performing on registration and login")
@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    private final UserService userService;
    private final AuthenticationService service;

//    Testing
    @GetMapping("/hi")
    public ResponseEntity<String> sayHi(){
        return ResponseEntity.ok("Hi from secured!");
    }

    @GetMapping("/all-user-data")
    public ResponseEntity<List<UserData>> allUsersData(){
        List<UserData> list = restTemplate.getForObject("http://user-data-service/api/v1/userdata/all-users-data",  List.class);
        return ResponseEntity.ok( list );
    }

    @GetMapping("/user-data/{id}")
    public ResponseEntity<UserData> userData(@PathVariable Long id){
        UserData userData = restTemplate.getForObject("http://user-data-service/api/v1/userdata/get-data/"+id,  UserData.class);
        return ResponseEntity.ok( userData );
    }

    @PutMapping("/update-user-data")
    public ResponseEntity<HttpStatus> updateData(@RequestBody UserData userData){
        restTemplate.put("http://user-data-service/api/v1/userdata/update-user-data", userData);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<AuthenticationResponse> changePassword(@PathVariable Long id, String password){
        return ResponseEntity.ok(userService.changePassword(id, password));
    }

    @PutMapping("/change-email/{id}")
    public ResponseEntity<AuthenticationResponse> changeEmail(@PathVariable Long id, String email){
        return ResponseEntity.ok(userService.changeEmail(id, email));
    }
//    this works
//    @PostMapping(path ="/authenticate",
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//            produces = {
//                    MediaType.APPLICATION_JSON_VALUE
//            })
//    public ResponseEntity<AuthenticationResponse> authenticate(
//            String email, String password
//    ) {
//        log.info("in auth method");
//        AuthenticationRequest request = new AuthenticationRequest(email, password);
//        log.info(request);
//        return ResponseEntity.ok(service.authenticate(request));
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }








}
