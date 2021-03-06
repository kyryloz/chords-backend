package com.robotnec.chords.service;

import com.robotnec.chords.persistence.entity.Song;
import com.robotnec.chords.web.dto.SongDto;

import java.util.List;
import java.util.Optional;

/**
 * @author zak <zak@robotnec.com>
 */
public interface SongService {
    Optional<Song> getSong(long id);

    Song createSong(Song song);

    List<Song> createSongs(List<Song> songs);

    Song updateSong(Song song);

    Song deleteSong(long id);

    List<Song> getSongs();

    List<Song> getRecentlyUpdatedSongs(int amount);
}
