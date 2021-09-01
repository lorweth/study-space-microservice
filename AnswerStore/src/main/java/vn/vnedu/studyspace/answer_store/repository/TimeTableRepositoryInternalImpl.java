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
import vn.vnedu.studyspace.answer_store.domain.TimeTable;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.TimeTableRowMapper;
import vn.vnedu.studyspace.answer_store.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the TimeTable entity.
 */
@SuppressWarnings("unused")
class TimeTableRepositoryInternalImpl implements TimeTableRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final TimeTableRowMapper timetableMapper;

    private static final Table entityTable = Table.aliased("time_table", EntityManager.ENTITY_ALIAS);

    public TimeTableRepositoryInternalImpl(R2dbcEntityTemplate template, EntityManager entityManager, TimeTableRowMapper timetableMapper) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.timetableMapper = timetableMapper;
    }

    @Override
    public Flux<TimeTable> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<TimeTable> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<TimeTable> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = TimeTableSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, TimeTable.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
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
    public Flux<TimeTable> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<TimeTable> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private TimeTable process(Row row, RowMetadata metadata) {
        TimeTable entity = timetableMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends TimeTable> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends TimeTable> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update TimeTable with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(TimeTable entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class TimeTableSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("title", table, columnPrefix + "_title"));
        columns.add(Column.aliased("time", table, columnPrefix + "_time"));
        columns.add(Column.aliased("note", table, columnPrefix + "_note"));
        columns.add(Column.aliased("user_login", table, columnPrefix + "_user_login"));

        return columns;
    }
}
