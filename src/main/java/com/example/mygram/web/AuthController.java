package com.example.mygram.web;

import com.example.mygram.payload.request.LoginRequest;
import com.example.mygram.payload.request.SignupRequest;
import com.example.mygram.payload.response.JWTTokenSuccessResponse;
import com.example.mygram.payload.response.MessageResponse;
import com.example.mygram.security.JWTTokenProvider;
import com.example.mygram.security.SecurityConstants;
import com.example.mygram.service.UserService;
import com.example.mygram.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider tokenProvider;

    @Autowired
    public AuthController(ResponseErrorValidation responseErrorValidation, UserService userService,
                          AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        userService.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

}
