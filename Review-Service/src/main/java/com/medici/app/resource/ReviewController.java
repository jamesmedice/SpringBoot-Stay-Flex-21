package com.medici.app.resource;

import com.medici.app.model.Review;
import com.medici.app.model.ReviewStatus;
import com.medici.app.model.SliceResponse;
import com.medici.app.service.ReviewService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.GET, value = "/slice")
    public ResponseEntity<SliceResponse<Review>> findAllReviews(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "status") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Slice<Review> slice = reviewService.findAll(pageable);
        SliceResponse<Review> payload = SliceResponse.<Review>builder()
                .content(slice.getContent())
                .number(slice.getNumber())
                .size(slice.getSize())
                .first(slice.isFirst())
                .last(slice.isLast())
                .sort(sort.toString())
                .build();
        return ResponseEntity.ok(payload);
    }

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.GET, value = "/all", produces = {"application/json"})
    public ResponseEntity<Slice<Review>> findAll(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "status") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.DEFAULT_DIRECTION.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Slice<Review> payload = reviewService.findAll(pageable);
        return ResponseEntity.ok(payload);
    }

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}", produces = {"application/json"})
    public ResponseEntity<List<Review>> findByUserId(@Parameter(name = "userId", description = "userId", required = true, in = ParameterIn.PATH) @PathVariable("userId") UUID userId) {
        List<Review> payload = reviewService.findByUserId(userId);
        return ResponseEntity.ok(payload);
    }

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.GET, value = "/status/{status}", produces = {"application/json"})
    public ResponseEntity<List<Review>> findByStatus(@Parameter(name = "status", description = "status", required = true, in = ParameterIn.PATH) @PathVariable("status") ReviewStatus status) {
        List<Review> payload = reviewService.findByStatus(status);
        return ResponseEntity.ok(payload);
    }

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.POST, value = "/save", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Void> save(@Valid @RequestBody Review request) {
        reviewService.save(request);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.PUT, value = "/update/{uuid}", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Void> update(@PathVariable("uuid") UUID uuid, @Valid @RequestBody Review review) {
        reviewService.update(uuid, review);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "REVIEW")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{uuid}", produces = {"application/json"})
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        reviewService.delete(uuid);
        return ResponseEntity.ok().build();
    }

}
