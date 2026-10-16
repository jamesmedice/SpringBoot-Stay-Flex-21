package com.medici.app.resource;

import com.medici.app.model.SliceResponse;
import com.medici.app.model.User;
import com.medici.app.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Tag(name = "USERS")
    @RequestMapping(value = "/save", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<Void> save(@RequestBody com.medici.app.model.User user) {
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "USERS")
    @RequestMapping(value = "/update/{id}", consumes = "application/json", produces = "application/json", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody com.medici.app.model.User user) {
        userService.update(id, user);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "USERS")
    @RequestMapping(value = "/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<com.medici.app.model.User> findById(@PathVariable UUID id) {
        User payload = userService.findUserById(id);
        return ResponseEntity.ok().body(payload);
    }

    @Tag(name = "USERS")
    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<com.medici.app.model.User> findByUsername(@RequestParam String username) {
        User payload = userService.findByUsername(username);
        return ResponseEntity.ok().body(payload);
    }

    @Tag(name = "USERS")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<SliceResponse<User>> findAll(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       @RequestParam(defaultValue = "username") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Slice<User> page = userService.findAll(pageable);
        SliceResponse<User> payload = SliceResponse.<User>builder()
                .content(page.getContent())
                .number(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .sort(sort.toString())
                .build();
        return ResponseEntity.ok().body(payload);
    }


    @Tag(name = "USERS")
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/page/all", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<Slice<User>> pageAll(@RequestParam(defaultValue = "0") Integer pageNumber,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(defaultValue = "username") String sortBy,
                                               @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Slice<User> payload = userService.findAll(pageable);
        return ResponseEntity.ok().body(payload);
    }
}
