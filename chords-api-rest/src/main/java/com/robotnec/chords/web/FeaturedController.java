package com.robotnec.chords.web;

import com.robotnec.chords.service.PerformerService;
import com.robotnec.chords.service.SongService;
import com.robotnec.chords.web.dto.FeaturedDto;
import com.robotnec.chords.web.dto.PerformerDto;
import com.robotnec.chords.web.dto.SongDto;
import com.robotnec.chords.web.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zak <zak@robotnec.com>
 */
@RestController
@RequestMapping(value = "/featured", produces = "application/json;charset=UTF-8")
public class FeaturedController {

    @Autowired
    SongService songService;

    @Autowired
    PerformerService performerService;

    @Autowired
    Mapper mapper;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<FeaturedDto> getFeatured() {
        List<SongDto> featuredSongs = mapper.mapAsList(songService.getRecentlyUpdatedSongs(20), SongDto.class);
        List<PerformerDto> allPerformers = mapper.mapAsList(performerService.getPerformers(), PerformerDto.class);
        return ResponseEntity.ok(new FeaturedDto(featuredSongs, allPerformers));
    }
}
