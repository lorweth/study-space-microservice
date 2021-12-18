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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupMember;
import vn.vnedu.studyspace.answer_store.repository.rowmapper.GroupMemberRowMapper;
import vn.vnedu.studyspace.answer_store.service.EntityManager;

/**
 * Spring Data SQL reactive custom repository implementation for the GroupMember entity.
 */
@SuppressWarnings("unused")
class GroupMemberRepositoryInternalImpl implements GroupMemberRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final GroupMemberRowMapper groupmemberMapper;

    private static final Table entityTable = Table.aliased("group_member", EntityManager.ENTITY_ALIAS);

    public GroupMemberRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        GroupMemberRowMapper groupmemberMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.groupmemberMapper = groupmemberMapper;
    }

    @Override
    public Flux<GroupMember> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<GroupMember> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<GroupMember> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = GroupMemberSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, GroupMember.class, pageable, criteria);
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
    public Flux<GroupMember> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<GroupMember> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private GroupMember process(Row row, RowMetadata metadata) {
        GroupMember entity = groupmemberMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends GroupMember> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends GroupMember> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update GroupMember with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(GroupMember entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
