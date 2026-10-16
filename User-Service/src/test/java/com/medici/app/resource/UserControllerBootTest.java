package com.medici.app.resource;

import com.medici.app.model.SliceResponse;
import com.medici.app.model.User;
import com.medici.app.repository.UserRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class
})
@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerBootTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void save_test() {
        User user = UserTestUtils.getUser(ConstantTestUtils.USERNAME);
        ResponseEntity<Void> response = restTemplate.postForEntity(User_URLS.SAVE.getUrl(), user, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void update_test() {
        User user = getUser(ConstantTestUtils.USERNAME_GAMMA);

        String url = UrlBuilderUtils.buildUrl(User_URLS.UPDATE.getUrl(), Map.of(UserUrlParam.ID.getKey(), user.id().toString()));
        User userUpdate = UserTestUtils.getUser(ConstantTestUtils.USERNAME_ALPHA, ConstantTestUtils.PHONE_A);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(userUpdate), Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    public void find_by_username_test() {
        User user = getUser(ConstantTestUtils.USERNAME_GAMMA);

        String url = UrlBuilderUtils.buildUrl(User_URLS.BASE_USER.getUrl(), null, Map.of(UserUrlParam.USERNAME.getKey(), user.username()));
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.id(), response.getBody().id());
        assertEquals(user.username(), response.getBody().username());
        assertEquals(user.email(), response.getBody().email());
        assertEquals(user.dob(), response.getBody().dob());
    }

    @Test
    @Order(4)
    public void find_by_id_test() {
        User user = getUser(ConstantTestUtils.USERNAME_ALPHA);

        String url = UrlBuilderUtils.buildUrl(User_URLS.FIND_BY_ID.getUrl(), Map.of(UserUrlParam.ID.getKey(), user.id()));
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.id(), response.getBody().id());
        assertEquals(user.username(), response.getBody().username());
        assertEquals(user.email(), response.getBody().email());
        assertEquals(user.phone(), response.getBody().phone());
        assertEquals(user.dob(), response.getBody().dob());
    }

    @Test
    @Order(5)
    public void find_all_test() {
        String url = UrlBuilderUtils.buildUrl(User_URLS.FIND_All.getUrl(), null);

        ResponseEntity<SliceResponse<User>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<SliceResponse<User>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent().isEmpty());
    }

    @Test
    @Order(6)
    public void find_all_pagination_test() {

        List<User> users = getUsers(
                ConstantTestUtils.USERNAME_ALPHA,
                ConstantTestUtils.USERNAME_BETA,
                ConstantTestUtils.USERNAME_CHI,
                ConstantTestUtils.USERNAME_DELTA,
                ConstantTestUtils.USERNAME_EPSILON,
                ConstantTestUtils.USERNAME_ETA,
                ConstantTestUtils.USERNAME_GAMMA,
                ConstantTestUtils.USERNAME_IOTA,
                ConstantTestUtils.USERNAME_KAPPA,
                ConstantTestUtils.USERNAME_LAMBDA,
                ConstantTestUtils.USERNAME_MU,
                ConstantTestUtils.USERNAME_NU,
                ConstantTestUtils.USERNAME_OMEGA,
                ConstantTestUtils.USERNAME_OMICRON,
                ConstantTestUtils.USERNAME_PHI,
                ConstantTestUtils.USERNAME_PI,
                ConstantTestUtils.USERNAME_PSI,
                ConstantTestUtils.USERNAME_RHO,
                ConstantTestUtils.USERNAME_SIGMA,
                ConstantTestUtils.USERNAME_TAU,
                ConstantTestUtils.USERNAME_THETA,
                ConstantTestUtils.USERNAME_XI,
                ConstantTestUtils.USERNAME_UPSILON,
                ConstantTestUtils.USERNAME_ZETA);

        String url = UrlBuilderUtils.buildUrl(User_URLS.FIND_All.getUrl(), null);
        ResponseEntity<SliceResponse<User>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<SliceResponse<User>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getContent().isEmpty());
    }

    private List<User> getUsers(String... usernames) {
        List<com.medici.app.entity.User> userEntities = Arrays.stream(usernames)
                .map(UserTestUtils::getUser)
                .map(UserUtils.modelToEntity)
                .toList();

        userRepository.saveAll(userEntities);

        return userEntities.stream().map(UserUtils.entityToModel).toList();
    }

    private User getUser(String usernameGama) {
        User user = UserTestUtils.getUser(usernameGama);
        com.medici.app.entity.User userEntity = UserUtils.modelToEntity.apply(user);
        userRepository.save(userEntity);
        return user;
    }

}