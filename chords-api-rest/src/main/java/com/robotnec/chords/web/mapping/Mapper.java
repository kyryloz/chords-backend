package com.robotnec.chords.web.mapping;

import com.robotnec.chords.persistence.entity.Performer;
import com.robotnec.chords.persistence.entity.Song;
import com.robotnec.chords.persistence.entity.SongSolrDocument;
import com.robotnec.chords.persistence.entity.user.ChordsUser;
import com.robotnec.chords.persistence.entity.user.Role;
import com.robotnec.chords.web.dto.ChordsUserDto;
import com.robotnec.chords.web.dto.PerformerDto;
import com.robotnec.chords.web.dto.SearchNodeDto;
import com.robotnec.chords.web.dto.SongDto;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zak <zak@robotnec.com>
 */
@Component
public class Mapper {

    private MapperFacade mapperFacade;

    @PostConstruct
    private void postConstruct() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Song.class, SongDto.class)
                .field("performer.id", "performerId")
                .field("performer.name", "performerName")
                .byDefault()
                .register();
        mapperFactory.classMap(Performer.class, PerformerDto.class)
                .customize(new CustomMapper<Performer, PerformerDto>() {
                    @Override
                    public void mapAtoB(Performer a, PerformerDto b, MappingContext context) {
                        Optional.ofNullable(a.getSongs())
                                .map(Set::size)
                                .ifPresent(b::setSongCount);
                    }
                })
                .byDefault()
                .register();
        mapperFactory.classMap(SongSolrDocument.class, SearchNodeDto.class)
                .field("id", "songId")
                .byDefault()
                .register();
        mapperFactory.classMap(User.class, ChordsUser.class)
                .field("email", "email")
                .field("id", "facebookUserId")
                .field("name", "name")
                .field("link", "facebookLink")
                .register();
        mapperFactory.classMap(ChordsUser.class, ChordsUserDto.class)
                .byDefault()
                .customize(new CustomMapper<ChordsUser, ChordsUserDto>() {
                    @Override
                    public void mapAtoB(ChordsUser a, ChordsUserDto b, MappingContext context) {
                        b.setAuthorities(a.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toList()));
                    }
                })
                .register();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public <T> T map(Object source, Class<T> destination) {
        return mapperFacade.map(source, destination);
    }

    public void map(Object source, Object destination) {
        mapperFacade.map(source, destination);
    }

    public <S, D> List<D> mapAsList(Iterable<S> iterable, Class<D> destination) {
        return mapperFacade.mapAsList(iterable, destination);
    }

    public <S, T> Page<T> mapAsPage(final Page<S> source, final Class<T> target) {
        List<T> content = mapAsList(source.getContent(), target);
        Pageable pageable = new PageRequest(source.getNumber(), source.getSize(), source.getSort());
        return new PageImpl<>(content, pageable, source.getTotalElements());
    }
}
