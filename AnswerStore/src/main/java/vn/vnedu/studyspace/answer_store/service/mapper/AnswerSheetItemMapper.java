package vn.vnedu.studyspace.answer_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.answer_store.domain.*;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetItemDTO;

/**
 * Mapper for the entity {@link AnswerSheetItem} and its DTO {@link AnswerSheetItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { AnswerSheetMapper.class })
public interface AnswerSheetItemMapper extends EntityMapper<AnswerSheetItemDTO, AnswerSheetItem> {
    @Mapping(target = "answerSheet", source = "answerSheet", qualifiedByName = "id")
    AnswerSheetItemDTO toDto(AnswerSheetItem s);
}
