package com.ascoproject.ascoproject.controller;

import com.ascoproject.ascoproject.model.AuthenticationRequest;
import com.ascoproject.ascoproject.model.AuthenticationResponse;
import com.ascoproject.ascoproject.model.ResponseAll;
import com.ascoproject.ascoproject.model.ResponseResult;
import com.ascoproject.ascoproject.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseResult<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request){
        ResponseAll<ResponseResult<AuthenticationResponse>> authenticate = authenticationService.authenticate(request);
        return ResponseEntity.status(authenticate.getStatus()).body(authenticate.getResponse());
    }

}
