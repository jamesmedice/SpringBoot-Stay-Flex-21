package com.medici.app.utils;

import com.medici.app.model.User;

import java.time.LocalDate;
import java.util.UUID;

import static com.medici.app.utils.ConstantTestUtils.EMAIL;
import static com.medici.app.utils.ConstantTestUtils.PHONE;

public class UserTestUtils {

    public static User getUser(String username, String phone) {
        User user = new User(UUID.randomUUID(), username, EMAIL, phone, LocalDate.now().minusYears(25L), LocalDate.now());
        return user;
    }

    public static User getUser(String username) {
        User user = getUser(username, PHONE);
        return user;
    }
}
