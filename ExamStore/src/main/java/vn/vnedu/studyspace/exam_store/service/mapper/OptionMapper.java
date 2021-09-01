package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.*;
import vn.vnedu.studyspace.exam_store.service.dto.OptionDTO;

/**
 * Mapper for the entity {@link Option} and its DTO {@link OptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface OptionMapper extends EntityMapper<OptionDTO, Option> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    OptionDTO toDto(Option s);
}
