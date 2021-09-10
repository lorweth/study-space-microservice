package vn.vnedu.studyspace.exam_store.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import vn.vnedu.studyspace.exam_store.domain.Option;

import java.util.List;

/**
 * Spring Data SQL repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByQuestion_Id(Long id);
}
