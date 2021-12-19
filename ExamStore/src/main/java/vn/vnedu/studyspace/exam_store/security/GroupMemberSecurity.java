package vn.vnedu.studyspace.exam_store.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;

import java.util.Optional;

@Component
public class GroupMemberSecurity {

    private final Logger log = LoggerFactory.getLogger(GroupMemberSecurity.class);

    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberSecurity(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    /**
     * Check permission of current user login.
     *
     * @param groupId the id of the group.
     * @param authority the authority.
     * @return true if user has permission, false or else.
     */
    public boolean hasPermission(Long groupId, String authority){
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(userLogin.isEmpty()){
            log.debug("User not logged in");
            return false;
        }

        Optional<GroupMember> member = groupMemberRepository.findByUserLoginAndGroupId(userLogin.get(), groupId);
        if(member.isEmpty()){
            log.debug("Member not exists");
            return false;
        }

        switch (authority){
            case "ADMIN":
                return member.get().getRole() == 2;
            case "MEMBER":
                return member.get().getRole() >= 1; // Because Admin can do anything
            case "WAITING":
                return member.get().getRole() == 0;
            default:
                return false;
        }
    }

    /**
     * Check permission on group of current user login.
     *
     * @param memberId id of the member (to get the group id).
     * @param authority the authority
     * @return true if user has permission, false or else.
     */
    public boolean hasPermissionOnGroupOfMember(Long memberId, String authority) {
        Optional<GroupMember> member = groupMemberRepository.findById(memberId);
        if (member.isEmpty()){
            log.debug("Member {} not exists", memberId);
            return false;
        }
        Long groupId = member.get().getGroupId();
        return hasPermission(groupId, authority);
    }

    public boolean notExists(Long groupId) {
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(userLogin.isEmpty()){
            log.debug("User not logged in");
            return false;
        }

        Optional<GroupMember> member = groupMemberRepository.findByUserLoginAndGroupId(userLogin.get(), groupId);
        return member.isEmpty();
    }
}
