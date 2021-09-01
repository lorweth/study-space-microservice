package vn.vnedu.studyspace.answer_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;

/**
 * Spring Data SQL reactive repository for the GroupTimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupTimeTableRepository extends R2dbcRepository<GroupTimeTable, Long>, GroupTimeTableRepositoryInternal {
    Flux<GroupTimeTable> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<GroupTimeTable> findAll();

    @Override
    Mono<GroupTimeTable> findById(Long id);

    @Override
    <S extends GroupTimeTable> Mono<S> save(S entity);
}

interface GroupTimeTableRepositoryInternal {
    <S extends GroupTimeTable> Mono<S> insert(S entity);
    <S extends GroupTimeTable> Mono<S> save(S entity);
    Mono<Integer> update(GroupTimeTable entity);

    Flux<GroupTimeTable> findAll();
    Mono<GroupTimeTable> findById(Long id);
    Flux<GroupTimeTable> findAllBy(Pageable pageable);
    Flux<GroupTimeTable> findAllBy(Pageable pageable, Criteria criteria);
}
