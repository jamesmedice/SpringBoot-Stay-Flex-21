package com.medici.app.resource;

import com.medici.app.service.BadgeHashManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/badge")
public class BadgeHashController {

    @Autowired
    BadgeHashManagerService badgeHashManagerService;

    @RequestMapping(method = RequestMethod.GET, value = "/hash", produces = {"application/json"})
    public ResponseEntity<String> getBadgeHash() {
        String payload = badgeHashManagerService.getBadgeHash();
        return ResponseEntity.ok(payload);
    }

}
