package com.robotnec.chords.service;

import com.robotnec.chords.persistence.entity.Chord;

import java.util.List;
import java.util.Optional;

public interface ChordService {
    Optional<Chord> findGuitarChordByName(String name);

    Chord createChord(Chord chord);

    Chord updateChord(Chord chord);

    Chord deleteChord(long id);

    List<Chord> getChords();

    Chord hydrateChord(Chord chord);
}
