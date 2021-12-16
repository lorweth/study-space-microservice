package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.exam_store.domain.Topic;
import vn.vnedu.studyspace.exam_store.service.dto.TopicDTO;

/**
 * Mapper for the entity {@link Topic} and its DTO {@link TopicDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TopicDTO toDtoId(Topic topic);
}
