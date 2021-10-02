package vn.vnedu.studyspace.group_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.group_store.domain.Group;
import vn.vnedu.studyspace.group_store.domain.GroupMember;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the GroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    boolean existsByUserLoginAndGroup_Id(String userLogin, Long id);

    Optional<GroupMember> findByUserLoginAndGroup_Id(String userLogin, Long id);

    Page<GroupMember> findByGroup_Id(Long id, Pageable pageable);

    List<GroupMember> findByGroupAndUserLoginContainingIgnoreCase(Group group, String userLogin);
}
