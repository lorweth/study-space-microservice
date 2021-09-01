package vn.vnedu.studyspace.group_store.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.group_store.domain.Group;

/**
 * Spring Data SQL repository for the Group entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {}
