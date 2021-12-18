package vn.vnedu.studyspace.answer_store.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupMember;

/**
 * Spring Data SQL reactive repository for the GroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupMemberRepository extends R2dbcRepository<GroupMember, Long>, GroupMemberRepositoryInternal {
    // just to avoid having unambigous methods
    @Override
    Flux<GroupMember> findAll();

    @Override
    Mono<GroupMember> findById(Long id);

    @Override
    <S extends GroupMember> Mono<S> save(S entity);
}

interface GroupMemberRepositoryInternal {
    <S extends GroupMember> Mono<S> insert(S entity);
    <S extends GroupMember> Mono<S> save(S entity);
    Mono<Integer> update(GroupMember entity);

    Flux<GroupMember> findAll();
    Mono<GroupMember> findById(Long id);
    Flux<GroupMember> findAllBy(Pageable pageable);
    Flux<GroupMember> findAllBy(Pageable pageable, Criteria criteria);
}
