package com.robotnec.chords.service.impl;

import com.robotnec.chords.exception.WrongArgumentException;
import com.robotnec.chords.persistence.entity.Performer;
import com.robotnec.chords.persistence.repository.PerformerRepository;
import com.robotnec.chords.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zak <zak@robotnec.com>
 */
@Service
public class PerformerServiceImpl implements PerformerService {

    @Autowired
    private PerformerRepository performerRepository;

    @Override
    public Optional<Performer> getPerformer(long id) {
        return Optional.ofNullable(performerRepository.findOne(id));
    }

    @Override
    public Optional<Performer> getPerformerByName(String name) {
        return Optional.ofNullable(performerRepository.findByName(name));
    }

    @Override
    public Page<Performer> getPerformers(Pageable pageable) {
        return performerRepository.findAll(pageable);
    }

    @Override
    public List<Performer> getPerformers() {
        List<Performer> performers = new ArrayList<>();
        performerRepository.findAll(new Sort(Sort.Direction.ASC, "name")).forEach(performers::add);
        return performers;
    }

    @Override
    public Performer createPerformer(Performer performer) {
        return performerRepository.save(performer);
    }

    @Override
    public Performer updatePerformer(Performer performer) {
        return createPerformer(performer);
    }

    @Override
    public Performer deletePerformer(long id) {
        return getPerformer(id)
                .map(this::deletePerformer)
                .orElseThrow(() -> new WrongArgumentException(String.format("Song with id '%s' not found", id)));
    }

    private Performer deletePerformer(Performer performer) {
        performerRepository.delete(performer.getId());
        return performer;
    }
}
