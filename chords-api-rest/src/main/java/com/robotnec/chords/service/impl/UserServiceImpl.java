package com.robotnec.chords.service.impl;

import com.robotnec.chords.persistence.entity.user.ChordsUser;
import com.robotnec.chords.persistence.repository.UserRepository;
import com.robotnec.chords.service.RoleService;
import com.robotnec.chords.service.UserService;
import com.robotnec.chords.user.ChordsUserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Transactional
    @Override
    public ChordsUser save(ChordsUser user) {
        // TODO handle better
        if (user.getEmail().equals("zapylaev@gmail.com")) {
            user.setRoles(roleService.getAdminRoles());
        } else {
            user.setRoles(roleService.getUserRoles());
        }

        ChordsUser saved = userRepository.save(user);
        log.debug("Saved user {}", user);
        return saved;
    }

    @Override
    public Optional<ChordsUser> findByEmail(String email) {
        log.debug("Try to find by email {}", email);

        ChordsUser user = userRepository.findByEmail(email);

        log.debug("Found user: {}", user);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<ChordsUser> getCurrent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable((ChordsUserWrapper) principal)
                .map(ChordsUserWrapper::getChordsUser);
    }
}