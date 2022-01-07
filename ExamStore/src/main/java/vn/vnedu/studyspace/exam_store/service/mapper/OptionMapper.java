package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.Option;
import vn.vnedu.studyspace.exam_store.service.dto.OptionDTO;

import java.util.Set;

/**
 * Mapper for the entity {@link Option} and its DTO {@link OptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { QuestionMapper.class })
public interface OptionMapper extends EntityMapper<OptionDTO, Option> {
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    OptionDTO toDto(Option s);

    @Named("toDtoIgnoreIsCorrect")
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    @Mapping(target = "isCorrect", ignore = true)
    OptionDTO toDtoIgnoreIsCorrect(Option s);

    @Named("setDTO")
    @Mapping(target = "question", source = "question", qualifiedByName = "id")
    Set<OptionDTO> toSetDTO(Set<Option> set);
}
