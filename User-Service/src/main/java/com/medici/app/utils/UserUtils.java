package com.medici.app.utils;

import com.medici.app.model.User;

import java.util.function.Function;

public class UserUtils {

    public static Function<com.medici.app.entity.User, User> entityToModel = entity ->
            new User(entity.getUuid(), entity.getUsername(), entity.getEmail(), entity.getPhone(), entity.getDob(), entity.getCreatedAt());

    public static Function<User, com.medici.app.entity.User> modelToEntity= model ->
            new com.medici.app.entity.User(model.id(), model.username(), model.email(), model.phone(), model.dob(), model.createdAt());

}
