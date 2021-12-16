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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.GroupTimeTableRowMapper;
import vn.vnedu.studyspace.answer_store.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the GroupTimeTable entity.
 */
@SuppressWarnings("unused")
class GroupTimeTableRepositoryInternalImpl implements GroupTimeTableRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GroupTimeTableRowMapper grouptimetableMapper;

    private static final Table entityTable = Table.aliased("group_time_table", EntityManager.ENTITY_ALIAS);

    public GroupTimeTableRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GroupTimeTableRowMapper grouptimetableMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.grouptimetableMapper = grouptimetableMapper;
    }

    @Override
    public Flux<GroupTimeTable> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<GroupTimeTable> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<GroupTimeTable> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = GroupTimeTableSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, GroupTimeTable.class, pageable, criteria);
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
    public Flux<GroupTimeTable> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<GroupTimeTable> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private GroupTimeTable process(Row row, RowMetadata metadata) {
        GroupTimeTable entity = grouptimetableMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends GroupTimeTable> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends GroupTimeTable> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update GroupTimeTable with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(GroupTimeTable entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
