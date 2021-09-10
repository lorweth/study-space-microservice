package vn.vnedu.studyspace.exam_store.repository;

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
    List<QuestionGroup> findByTopic_IdAndUserLogin(Long id, String userLogin);
}
