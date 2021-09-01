package vn.vnedu.studyspace.group_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.group_store.domain.*;
import vn.vnedu.studyspace.group_store.service.dto.GroupDTO;

/**
 * Mapper for the entity {@link Group} and its DTO {@link GroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GroupDTO toDtoId(Group group);
}
