package com.robotnec.chords.web;

import com.robotnec.chords.persistence.entity.Performer;
import com.robotnec.chords.persistence.entity.Song;
import com.robotnec.chords.service.PerformerService;
import com.robotnec.chords.web.dto.PerformerDto;
import com.robotnec.chords.web.dto.SongDto;
import com.robotnec.chords.web.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/performers", produces = "application/json;charset=UTF-8")
public class PerformersController {

    @Autowired
    private PerformerService performerService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Performer>> getPerformers() {
        return ResponseEntity.ok(performerService.getPerformers());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PerformerDto> createPerformer(@RequestBody PerformerDto performerDto) {
        return Optional.of(performerDto)
                .map(v -> mapper.map(v, Performer.class))
                .map(performerService::createPerformer)
                .map(v -> mapper.map(v, PerformerDto.class))
                .map(ResponseEntity::ok)
                .orElseThrow(IllegalStateException::new);
    }
}