package com.medici.app.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.medici.app.model.Review;
import com.medici.app.model.SliceResponse;
import com.medici.app.repository.ReviewRepository;
import com.medici.app.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class, org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class})
@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    public void init() {
        reviewRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void save_test() {
        Review review = ReviewTestUtils.getReview();
        ResponseEntity<Void> response = testRestTemplate.postForEntity(Review_URLS.SAVE.getUrl(), review, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void findByUserId_test() {
        UUID userId = UUID.randomUUID();
        List<Review> reviews = givenReviews(userId, 5);
        String url = UrlBuilderUtils.buildUrl(Review_URLS.FIND_BY_USER_ID.getUrl(), Map.of(ReviewUrlParam.USER_ID.getKey(), userId), null);
        ResponseEntity<List<Review>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews.size(), response.getBody().size());
    }

    @Test
    @Order(3)
    public void findByStatus_test() {
        List<Review> reviews = givenReviews();
        String url = UrlBuilderUtils.buildUrl(Review_URLS.FIND_BY_STATUS.getUrl(), Map.of(ReviewUrlParam.STATUS.getKey(), reviews.get(0).status()), null);
        ResponseEntity<List<Review>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews.size(), response.getBody().size());
    }

    @Test
    @Order(4)
    public void update_test() {
        Review review = givenReview();
        String url = UrlBuilderUtils.buildUrl(Review_URLS.UPDATE.getUrl(), Map.of(ReviewUrlParam.ID.getKey(), review.id()), null);
        ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(review), Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void delete_test() {
        Review review = givenReview();
        String url = UrlBuilderUtils.buildUrl(Review_URLS.DELETE.getUrl(), Map.of(ReviewUrlParam.ID.getKey(), review.id()), null);
        ResponseEntity<Void> response = testRestTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(6)
    public void findAll_test() {
        List<Review> reviews = givenReviews();
        String url = UrlBuilderUtils.buildUrl(Review_URLS.ALL.getUrl());
        ResponseEntity<JsonNode> response = testRestTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JsonNode body = response.getBody();
        JsonNode content = body.get(ConstantTestUtils.CONTENT);
        assertEquals(reviews.size(), content.size());
    }

    @Test
    @Order(7)
    public void findSlice_test() {
        List<Review> reviews = givenReviews();
        String url = UrlBuilderUtils.buildUrl(Review_URLS.SLICE.getUrl());
        ResponseEntity<SliceResponse<Review>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<SliceResponse<Review>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Review givenReview() {
        Review review = ReviewTestUtils.getReview();
        save(review);
        return review;
    }

    private List<Review> givenReviews() {
        return givenReviews(UUID.randomUUID(), 10);
    }

    private List<Review> givenReviews(UUID userId, int count) {
        List<Review> reviews = IntStream.range(0, count).mapToObj(i -> ReviewTestUtils.getReview(userId)).toList();
        saveAll(reviews);
        return reviews;
    }

    private void save(Review review) {
        reviewRepository.save(ReviewUtils.modelToEntity.apply(review));
    }

    private void saveAll(List<Review> reviews) {
        reviewRepository.saveAll(reviews.stream().map(review -> ReviewUtils.modelToEntity.apply(review)).toList());
    }
}
