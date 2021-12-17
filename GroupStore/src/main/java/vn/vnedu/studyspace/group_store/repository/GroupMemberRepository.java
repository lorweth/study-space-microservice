package vn.vnedu.studyspace.group_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.group_store.domain.Group;
import vn.vnedu.studyspace.group_store.domain.GroupMember;

import java.util.Optional;

/**
 * Spring Data SQL repository for the GroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserLoginAndGroupId(String userLogin, Long groupId);
    Page<GroupMember> findAllByGroupId(Long groupId, Pageable pageable);
    Optional<Long> countByGroupId(Long groupId);
    Void deleteByGroupId(Long groupId);
}
