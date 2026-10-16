package com.medici.app.service;

import com.medici.app.constants.ApplicationConstants;
import com.medici.app.model.User;
import com.medici.app.repository.UserRepository;
import com.medici.app.utils.UserUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.medici.app.exceptions.LogExceptionMessages.ENTITY_NOT_FOUND;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    public void save(User user) throws IllegalArgumentException {

        if (isEmailAlreadyTaken().test(user.email()))
            throw new IllegalArgumentException(ApplicationConstants.EMAIL_ALREADY_IN_USE);

        com.medici.app.entity.User entity = new com.medici.app.entity.User();
        entity.setUuid(UUID.randomUUID());
        entity.setUsername(user.username());
        entity.setDob(user.dob());
        entity.setPhone(user.phone());
        entity.setEmail(user.email());
        entity.setCreatedAt(user.createdAt());

        validateEntity(entity);

        userRepository.save(entity);
    }

    public void update(UUID id, User user) throws EntityNotFoundException, IllegalArgumentException {
        com.medici.app.entity.User entity = findById(id);

        if (!entity.getEmail().equals(user.email()))
            if (isEmailAlreadyTaken().test(user.email()))
                throw new IllegalArgumentException(ApplicationConstants.EMAIL_ALREADY_IN_USE);

        entity.setUsername(user.username());
        entity.setDob(user.dob());
        entity.setPhone(user.phone());
        entity.setEmail(user.email());

        validateEntity(entity);
        userRepository.save(entity);
    }

    public User findByUsername(String username) {
        com.medici.app.entity.User entity = userRepository.findByUsername(username);
        return UserUtils.entityToModel.apply(entity);
    }

    public User findUserById(UUID id) throws EntityNotFoundException {
        com.medici.app.entity.User entity = findById(id);
        return UserUtils.entityToModel.apply(entity);
    }

    public com.medici.app.entity.User findById(UUID id) throws EntityNotFoundException {
        Optional<com.medici.app.entity.User> opEntity = userRepository.findById(id);
        if (opEntity.isEmpty())
            throw new EntityNotFoundException(MessageFormat.format(ENTITY_NOT_FOUND, id));

        return opEntity.get();
    }

    @Secured("ROLE_ADMIN")
    public Page<User> findAll(Pageable pageable) {
        Page<com.medici.app.entity.User> users = userRepository.findAll(pageable);
        return users.map(entity -> UserUtils.entityToModel.apply(entity));
    }

    private <T> void validateEntity(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(ApplicationConstants.SEMI_COLON));
            throw new IllegalArgumentException(errorMessages);
        }
    }

    private Predicate<String> isEmailAlreadyTaken() {
        return email -> userRepository.findByEmail(email) != null;
    }

}
