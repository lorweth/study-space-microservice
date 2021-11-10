package vn.vnedu.studyspace.exam_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;

import java.util.List;

/**
 * Spring Data SQL repository for the QuestionGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {
    Page<QuestionGroup> findAllByUserLogin(String userLogin, Pageable pageable);

    Page<QuestionGroup> findAllByGroupId(Long groupId, Pageable pageable);
}
