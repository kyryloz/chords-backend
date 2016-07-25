package com.robotnec.chords.web;

import com.robotnec.chords.exception.WrongArgumentException;
import com.robotnec.chords.persistence.entity.Song;
import com.robotnec.chords.service.SongService;
import com.robotnec.chords.web.dto.SongDto;
import com.robotnec.chords.web.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/songs", produces = "application/json;charset=UTF-8")
public class SongsController {

    @Autowired
    private SongService songService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<SongDto>> getSongs() {
        return ResponseEntity.ok(songService.getSongs());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<SongDto> getSong(@PathVariable("id") final Long id) {
        return Optional.of(id)
                .flatMap(v -> songService.getSong(v))
                .map(v -> mapper.map(v, SongDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new WrongArgumentException(String.format("Song with id '%s' not found", id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<SongDto> createSong(@RequestBody SongDto songDto) {
        return Optional.of(songDto)
                .map(v -> mapper.map(v, Song.class))
                .map(songService::createSong)
                .map(v -> mapper.map(v, SongDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(IllegalStateException::new);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<SongDto> updateSong(@PathVariable("id") final Long id,
                                              @RequestBody final SongDto songDto) {
        return Optional.of(songDto)
                .map(v -> mapper.map(v, Song.class))
                .map(v -> setId(v, id))
                .map(songService::updateSong)
                .map(v -> mapper.map(v, SongDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(IllegalStateException::new);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<SongDto> deleteSong(@PathVariable("id") final Long id) {
        return Optional.of(id)
                .map(songService::deleteSong)
                .map(v -> mapper.map(v, SongDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new WrongArgumentException(String.format("Song with id '%s' not found", id)));
    }

    private Song setId(Song song, Long id) {
        song.setId(id);
        return song;
    }
}