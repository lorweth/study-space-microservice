package vn.vnedu.studyspace.exam_store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.exam_store.domain.Question;

import java.util.List;

/**
 * Spring Data SQL repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByQuestionGroupId(Long repoId, Pageable pageable);
    List<Question> findByQuestionGroupId(Long repoId);
    Long countByQuestionGroupId(Long repoId);
}
