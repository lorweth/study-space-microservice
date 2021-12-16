package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.Question;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionGroupMapper.class })
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "questionGroup", source = "questionGroup", qualifiedByName = "id")
    QuestionDTO toDto(Question s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoId(Question question);
}
