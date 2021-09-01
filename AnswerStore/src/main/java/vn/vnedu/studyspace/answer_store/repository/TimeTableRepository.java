package vn.vnedu.studyspace.answer_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.TimeTable;

/**
 * Spring Data SQL reactive repository for the TimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeTableRepository extends R2dbcRepository<TimeTable, Long>, TimeTableRepositoryInternal {
    Flux<TimeTable> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<TimeTable> findAll();

    @Override
    Mono<TimeTable> findById(Long id);

    @Override
    <S extends TimeTable> Mono<S> save(S entity);
}

interface TimeTableRepositoryInternal {
    <S extends TimeTable> Mono<S> insert(S entity);
    <S extends TimeTable> Mono<S> save(S entity);
    Mono<Integer> update(TimeTable entity);

    Flux<TimeTable> findAll();
    Mono<TimeTable> findById(Long id);
    Flux<TimeTable> findAllBy(Pageable pageable);
    Flux<TimeTable> findAllBy(Pageable pageable, Criteria criteria);
}
