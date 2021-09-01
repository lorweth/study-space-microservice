package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.*;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;

/**
 * Mapper for the entity {@link Exam} and its DTO {@link ExamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExamDTO toDtoId(Exam exam);
}
