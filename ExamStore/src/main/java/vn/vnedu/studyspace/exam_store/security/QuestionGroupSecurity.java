package vn.vnedu.studyspace.exam_store.security;

import org.springframework.stereotype.Component;
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.web.rest.QuestionGroupResource;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

import java.util.Objects;
import java.util.Optional;

@Component
public class QuestionGroupSecurity {

    private final QuestionGroupRepository questionGroupRepository;

    private final GroupMemberRepository groupMemberRepository;

    public QuestionGroupSecurity(QuestionGroupRepository questionGroupRepository, GroupMemberRepository groupMemberRepository) {
        this.questionGroupRepository = questionGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public boolean hasPermission(Long repoId, String authority){
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(userLogin.isEmpty()){
            throw new BadRequestAlertException("User not logged in", "ExamStore", "userNotLoggedIn");
        }

        // find question group by "repoId"
        Optional<QuestionGroup> questionGroup = questionGroupRepository.findById(repoId);
        if(questionGroup.isEmpty()){
            throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
        }

        // check group role if groupId not null
        if(questionGroup.get().getGroupId() != null) {
            Optional<GroupMember> groupMember = groupMemberRepository.findByUserLoginAndGroupId(userLogin.get(), questionGroup.get().getGroupId());
            if (groupMember.isEmpty()) {
                throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
            }

            switch (authority){
                case "ADMIN":
                    return groupMember.get().getRole() == 2;
                case "MEMBER":
                    return groupMember.get().getRole() >= 1;
                default:
                    return false;
            }
        }

        // check permission of user in questionGroup
        return Objects.equals(questionGroup.get().getUserLogin(), userLogin.get());
    }

}
