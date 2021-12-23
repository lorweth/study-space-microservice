package vn.vnedu.studyspace.answer_store.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.AnswerSheetItemRowMapper;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.AnswerSheetRowMapper;
import vn.vnedu.studyspace.answer_store.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the AnswerSheetItem entity.
 */
@SuppressWarnings("unused")
class AnswerSheetItemRepositoryInternalImpl implements AnswerSheetItemRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final AnswerSheetRowMapper answersheetMapper;
    private final AnswerSheetItemRowMapper answersheetitemMapper;

    private static final Table entityTable = Table.aliased("answer_sheet_item", EntityManager.ENTITY_ALIAS);
    private static final Table answerSheetTable = Table.aliased("answer_sheet", "answerSheet");

    public AnswerSheetItemRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        AnswerSheetRowMapper answersheetMapper,
        AnswerSheetItemRowMapper answersheetitemMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.answersheetMapper = answersheetMapper;
        this.answersheetitemMapper = answersheetitemMapper;
    }

    @Override
    public Flux<AnswerSheetItem> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<AnswerSheetItem> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    @Override
    public Flux<AnswerSheetItem> findAllByAnswerSheetId(Long sheetId, Pageable pageable) {
        return createQuery(pageable, where("answerSheetId").is(sheetId)).all();
    }

    RowsFetchSpec<AnswerSheetItem> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AnswerSheetItemSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(AnswerSheetSqlHelper.getColumns(answerSheetTable, "answerSheet"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(answerSheetTable)
            .on(Column.create("answer_sheet_id", entityTable))
            .equals(Column.create("id", answerSheetTable));

        String select = entityManager.createSelect(selectFrom, AnswerSheetItem.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(crit ->
                new StringBuilder(select)
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(alias)
                    .append(".")
                    .append(crit.toString())
                    .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<AnswerSheetItem> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<AnswerSheetItem> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private AnswerSheetItem process(Row row, RowMetadata metadata) {
        AnswerSheetItem entity = answersheetitemMapper.apply(row, "e");
        entity.setAnswerSheet(answersheetMapper.apply(row, "answerSheet"));
        return entity;
    }

    @Override
    public Mono<Long> countBy(Criteria criteria) {
        return r2dbcEntityTemplate.count(query(criteria), AnswerSheetItem.class);
    }

    @Override
    public <S extends AnswerSheetItem> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends AnswerSheetItem> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update AnswerSheetItem with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(AnswerSheetItem entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
