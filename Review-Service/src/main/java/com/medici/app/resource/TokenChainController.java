package com.medici.app.resource;

import com.medici.app.service.TokenChainManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenChainController {

    @Autowired
    TokenChainManagerService tokenChainManagerService;

    @RequestMapping(method = RequestMethod.GET, value = "/code", produces = {"application/json"})
    public ResponseEntity<String> getToken() {
        String payload = tokenChainManagerService.getToken();
        return ResponseEntity.ok(payload);
    }

}
