package com.robotnec.chords.service;


import com.robotnec.chords.persistence.entity.user.ChordsUser;

import java.util.Optional;

public interface UserService {
    ChordsUser save(ChordsUser user);

    Optional<ChordsUser> findByUsername(String username);

    Optional<ChordsUser> findByEmail(String email);
}