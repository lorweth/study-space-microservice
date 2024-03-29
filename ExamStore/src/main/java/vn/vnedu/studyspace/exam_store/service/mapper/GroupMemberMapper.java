package vn.vnedu.studyspace.exam_store.service.mapper;

import org.mapstruct.Mapper;
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.service.dto.GroupMemberDTO;

/**
 * Mapper for the entity {@link GroupMember} and its DTO {@link GroupMemberDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GroupMemberMapper extends EntityMapper<GroupMemberDTO, GroupMember> {}
