package com.ascoproject.ascoproject.controller;

import com.ascoproject.ascoproject.model.auth.AuthenticationRequest;
import com.ascoproject.ascoproject.model.auth.AuthenticationResponse;
import com.ascoproject.ascoproject.model.responce.ResponseAll;
import com.ascoproject.ascoproject.model.responce.ResponseResult;
import com.ascoproject.ascoproject.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseResult<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request){
        ResponseAll<ResponseResult<AuthenticationResponse>> authenticate = authenticationService.authenticate(request);
        return ResponseEntity.status(authenticate.getStatus()).body(authenticate.getResponse());
    }

}
