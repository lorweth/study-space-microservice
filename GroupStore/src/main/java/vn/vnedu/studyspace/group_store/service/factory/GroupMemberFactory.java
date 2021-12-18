package vn.vnedu.studyspace.group_store.service.factory;

import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.service.dto.MemberDTO;

public class GroupMemberFactory {

    public static MemberDTO getMemberDTO(GroupMemberDTO groupMemberDTO) {
        final MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(groupMemberDTO.getId());
        memberDTO.setUserLogin(groupMemberDTO.getUserLogin());
        memberDTO.setRole(groupMemberDTO.getRole());
        memberDTO.setGroup(groupMemberDTO.getGroup().getId());
        return memberDTO;
    }

}
