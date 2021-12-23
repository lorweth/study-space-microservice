package vn.vnedu.studyspace.answer_store.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.repository.GroupMemberRepository;

@Component
public class GroupMemberSecurity {

    private final Logger log = LoggerFactory.getLogger(GroupMemberSecurity.class);

    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberSecurity(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    protected Mono<Boolean> compareRole(Long groupId, String authority){
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userLogin -> groupMemberRepository.findByUserLoginAndGroupId(userLogin, groupId))
            .map(
                member -> {
                    switch (authority){
                        case "ADMIN":
                            return member.getRole() == 2;
                        case "MEMBER":
                            return member.getRole() >= 1; // Because Admin can do anything
                        case "WAITING":
                            return member.getRole() == 0;
                        default:
                            return false;
                    }
                }
            );
    }

    /**
     * Check permission of current user login.
     *
     * @param groupId the id of the group.
     * @param authority the authority.
     * @return true if user has permission, false or else.
     */
    public Boolean hasPermission(Long groupId, String authority){
        log.debug("Request to check current userLogin has permission {} in group {}", authority, groupId);
        return compareRole(groupId, authority).block();
    }

    public Boolean notExists(Long groupId) {
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userLogin -> groupMemberRepository.findByUserLoginAndGroupId(userLogin, groupId))
            .map(member -> {
                if (member != null){
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            })
            .block();
    }
}
