package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.*;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionGroupMapper.class, OptionMapper.class })
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "repo", source = "repo", qualifiedByName = "id")
    @Mapping(target = "options", source = "options")
    QuestionDTO toDto(Question s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoId(Question question);

    @Named("entityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Question toEntityId(QuestionDTO d);
}
