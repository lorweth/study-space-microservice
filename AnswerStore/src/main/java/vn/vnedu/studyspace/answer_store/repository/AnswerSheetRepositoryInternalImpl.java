package vn.vnedu.studyspace.answer_store.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
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
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.AnswerSheetRowMapper;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.GroupTimeTableRowMapper;
import vn.vnedu.studyspace.answer_store.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the AnswerSheet entity.
 */
@SuppressWarnings("unused")
class AnswerSheetRepositoryInternalImpl implements AnswerSheetRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GroupTimeTableRowMapper grouptimetableMapper;
    private final AnswerSheetRowMapper answersheetMapper;

    private static final Table entityTable = Table.aliased("answer_sheet", EntityManager.ENTITY_ALIAS);
    private static final Table groupTimeTableTable = Table.aliased("group_time_table", "groupTimeTable");

    public AnswerSheetRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GroupTimeTableRowMapper grouptimetableMapper,
        AnswerSheetRowMapper answersheetMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.grouptimetableMapper = grouptimetableMapper;
        this.answersheetMapper = answersheetMapper;
    }

    @Override
    public Flux<AnswerSheet> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<AnswerSheet> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    @Override
    public Flux<AnswerSheet> findAllByExamIdAndUserLogin(Long examId, String userLogin) {
        return createQuery(null, where("exam_id").is(examId).and(where("user_login").is(userLogin))).all();
    }

    RowsFetchSpec<AnswerSheet> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AnswerSheetSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(GroupTimeTableSqlHelper.getColumns(groupTimeTableTable, "groupTimeTable"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(groupTimeTableTable)
            .on(Column.create("group_time_table_id", entityTable))
            .equals(Column.create("id", groupTimeTableTable));

        String select = entityManager.createSelect(selectFrom, AnswerSheet.class, pageable, criteria);
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
    public Flux<AnswerSheet> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<AnswerSheet> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private AnswerSheet process(Row row, RowMetadata metadata) {
        AnswerSheet entity = answersheetMapper.apply(row, "e");
        entity.setGroupTimeTable(grouptimetableMapper.apply(row, "groupTimeTable"));
        return entity;
    }

    @Override
    public <S extends AnswerSheet> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends AnswerSheet> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update AnswerSheet with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(AnswerSheet entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
