package vn.vnedu.studyspace.answer_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;

/**
 * Spring Data SQL reactive repository for the AnswerSheet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerSheetRepository extends R2dbcRepository<AnswerSheet, Long>, AnswerSheetRepositoryInternal {
    Flux<AnswerSheet> findAllBy(Pageable pageable);

    @Query("SELECT * FROM answer_sheet entity WHERE entity.group_time_table_id = :id")
    Flux<AnswerSheet> findByGroupTimeTable(Long id);

    @Query("SELECT * FROM answer_sheet entity WHERE entity.group_time_table_id IS NULL")
    Flux<AnswerSheet> findAllWhereGroupTimeTableIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<AnswerSheet> findAll();

    @Override
    Mono<AnswerSheet> findById(Long id);

    @Override
    <S extends AnswerSheet> Mono<S> save(S entity);
}

interface AnswerSheetRepositoryInternal {
    <S extends AnswerSheet> Mono<S> insert(S entity);
    <S extends AnswerSheet> Mono<S> save(S entity);
    Mono<Integer> update(AnswerSheet entity);

    Flux<AnswerSheet> findAll();
    Mono<AnswerSheet> findById(Long id);
    Flux<AnswerSheet> findAllBy(Pageable pageable);
    Flux<AnswerSheet> findAllBy(Pageable pageable, Criteria criteria);
}
