package vn.vnedu.studyspace.answer_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.answer_store.domain.*;
import vn.vnedu.studyspace.answer_store.service.dto.GroupTimeTableDTO;

/**
 * Mapper for the entity {@link GroupTimeTable} and its DTO {@link GroupTimeTableDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupTimeTableMapper extends EntityMapper<GroupTimeTableDTO, GroupTimeTable> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GroupTimeTableDTO toDtoId(GroupTimeTable groupTimeTable);
}
