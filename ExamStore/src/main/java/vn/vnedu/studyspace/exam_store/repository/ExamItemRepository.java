package vn.vnedu.studyspace.exam_store.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.exam_store.domain.ExamItem;

import java.util.List;

/**
 * Spring Data SQL repository for the ExamItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExamItemRepository extends JpaRepository<ExamItem, Long> {
    void deleteByExamId(Long examId);

    List<ExamItem> findAllByExamId(Long examId);
}
