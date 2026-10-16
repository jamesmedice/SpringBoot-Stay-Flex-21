package com.medici.app.resource;

import com.medici.app.service.CredentialsManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    @Autowired
    CredentialsManagerService credentialsManagerService;

    @RequestMapping(method = RequestMethod.GET, value = "/mesh", produces = {"application/json"})
    public ResponseEntity<String> getMesh() {
        String payload = credentialsManagerService.getMeshup();
        return ResponseEntity.ok(payload);
    }
}
