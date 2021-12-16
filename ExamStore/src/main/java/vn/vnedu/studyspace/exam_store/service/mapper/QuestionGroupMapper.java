package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionGroupDTO;

/**
 * Mapper for the entity {@link QuestionGroup} and its DTO {@link QuestionGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = { TopicMapper.class })
public interface QuestionGroupMapper extends EntityMapper<QuestionGroupDTO, QuestionGroup> {
    @Mapping(target = "topic", source = "topic", qualifiedByName = "id")
    QuestionGroupDTO toDto(QuestionGroup s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionGroupDTO toDtoId(QuestionGroup questionGroup);
}
