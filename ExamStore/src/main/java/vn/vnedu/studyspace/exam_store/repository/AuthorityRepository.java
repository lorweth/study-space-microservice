package vn.vnedu.studyspace.exam_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.vnedu.studyspace.exam_store.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
