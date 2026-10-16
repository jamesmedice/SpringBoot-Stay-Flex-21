package com.medici.app.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medici.app.model.User;
import com.medici.app.repository.UserRepository;
import com.medici.app.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration.class
})
@Slf4j
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void save_test() throws Exception {
        User user = UserTestUtils.getUser(ConstantTestUtils.USERNAME);
        mockMvc.perform(post(User_URLS.SAVE.getUrl())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    void update_test() throws Exception {
        User user = getUser(ConstantTestUtils.USERNAME_GAMMA);

        String url = UrlBuilderUtils.buildUrl(User_URLS.UPDATE.getUrl(), Map.of(UserUrlParam.ID.getKey(), user.id().toString()));
        User userUpdate = UserTestUtils.getUser(ConstantTestUtils.USERNAME_ALPHA, ConstantTestUtils.PHONE_A);

        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void find_by_username_test() throws Exception {
        User user = getUser(ConstantTestUtils.USERNAME_GAMMA);
        String url = UrlBuilderUtils.buildUrl(User_URLS.BASE_USER.getUrl(), null, Map.of(UserUrlParam.USERNAME.getKey(), user.username()));

        ResultActions result = mockMvc.perform(get(url));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(user.id().toString()));
        result.andExpect(jsonPath("$.username").value(user.username()));
    }

    @Test
    @Order(5)
    void find_all_test() throws Exception {
        String url = UrlBuilderUtils.buildUrl(User_URLS.PAGE_All.getUrl(), null);

        ResultActions result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content", hasSize(0)));
        result.andExpect(jsonPath("$.first").value(true));
    }

    @Test
    @Order(6)
    void find_all_pagination_test() throws Exception {
        List<User> users = getUsers(
                ConstantTestUtils.USERNAME_ALPHA,
                ConstantTestUtils.USERNAME_BETA,
                ConstantTestUtils.USERNAME_CHI
        );

        String url = UrlBuilderUtils.buildUrl(User_URLS.FIND_All.getUrl(), null);

        ResultActions result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content", hasSize(users.size())));
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
