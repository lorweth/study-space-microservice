package vn.vnedu.studyspace.answer_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;

/**
 * Spring Data SQL reactive repository for the AnswerSheetItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnswerSheetItemRepository extends R2dbcRepository<AnswerSheetItem, Long>, AnswerSheetItemRepositoryInternal {
    Flux<AnswerSheetItem> findAllBy(Pageable pageable);

    @Query("SELECT * FROM answer_sheet_item entity WHERE entity.answer_sheet_id = :id")
    Flux<AnswerSheetItem> findByAnswerSheet(Long id);

    @Query("SELECT * FROM answer_sheet_item entity WHERE entity.answer_sheet_id IS NULL")
    Flux<AnswerSheetItem> findAllWhereAnswerSheetIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<AnswerSheetItem> findAll();

    @Override
    Mono<AnswerSheetItem> findById(Long id);

    @Override
    <S extends AnswerSheetItem> Mono<S> save(S entity);
}

interface AnswerSheetItemRepositoryInternal {
    <S extends AnswerSheetItem> Mono<S> insert(S entity);
    <S extends AnswerSheetItem> Mono<S> save(S entity);
    Mono<Integer> update(AnswerSheetItem entity);
    Mono<Long> countBy(Criteria criteria);

    Flux<AnswerSheetItem> findAll();
    Mono<AnswerSheetItem> findById(Long id);
    Flux<AnswerSheetItem> findAllByAnswerSheetId(Long sheetId, Pageable pageable);
    Flux<AnswerSheetItem> findAllBy(Pageable pageable);
    Flux<AnswerSheetItem> findAllBy(Pageable pageable, Criteria criteria);
}
