package vn.vnedu.studyspace.group_store.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.group_store.domain.GroupMember;

/**
 * Spring Data SQL repository for the GroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {}
