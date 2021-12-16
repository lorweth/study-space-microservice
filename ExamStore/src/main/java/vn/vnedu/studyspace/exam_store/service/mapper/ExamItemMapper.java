package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.ExamItem;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;

/**
 * Mapper for the entity {@link ExamItem} and its DTO {@link ExamItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionGroupMapper.class, ExamMapper.class })
public interface ExamItemMapper extends EntityMapper<ExamItemDTO, ExamItem> {
    @Mapping(target = "questionGroup", source = "questionGroup", qualifiedByName = "id")
    @Mapping(target = "exam", source = "exam", qualifiedByName = "id")
    ExamItemDTO toDto(ExamItem s);
}
