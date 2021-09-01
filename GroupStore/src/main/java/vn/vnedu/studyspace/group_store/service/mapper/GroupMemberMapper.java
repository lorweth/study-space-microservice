package vn.vnedu.studyspace.group_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.group_store.domain.*;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;

/**
 * Mapper for the entity {@link GroupMember} and its DTO {@link GroupMemberDTO}.
 */
@Mapper(componentModel = "spring", uses = { GroupMapper.class })
public interface GroupMemberMapper extends EntityMapper<GroupMemberDTO, GroupMember> {
    @Mapping(target = "group", source = "group", qualifiedByName = "id")
    GroupMemberDTO toDto(GroupMember s);
}
