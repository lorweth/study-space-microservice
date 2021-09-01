package vn.vnedu.studyspace.answer_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.answer_store.domain.*;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetDTO;

/**
 * Mapper for the entity {@link AnswerSheet} and its DTO {@link AnswerSheetDTO}.
 */
@Mapper(componentModel = "spring", uses = { GroupTimeTableMapper.class })
public interface AnswerSheetMapper extends EntityMapper<AnswerSheetDTO, AnswerSheet> {
    @Mapping(target = "groupTimeTable", source = "groupTimeTable", qualifiedByName = "id")
    AnswerSheetDTO toDto(AnswerSheet s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AnswerSheetDTO toDtoId(AnswerSheet answerSheet);
}
