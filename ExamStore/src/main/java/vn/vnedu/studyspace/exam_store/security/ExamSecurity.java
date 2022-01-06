package vn.vnedu.studyspace.exam_store.security;

import org.springframework.stereotype.Component;
import vn.vnedu.studyspace.exam_store.domain.Exam;
import vn.vnedu.studyspace.exam_store.repository.ExamRepository;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

import java.util.Objects;
import java.util.Optional;

@Component
public class ExamSecurity extends GroupMemberSecurity{

    private final ExamRepository examRepository;

    public ExamSecurity(GroupMemberRepository groupMemberRepository, ExamRepository examRepository) {
        super(groupMemberRepository);
        this.examRepository = examRepository;
    }

    @Override
    public boolean hasPermission(Long examId, String authority) {
        // Find the "id" exam
        Exam exam = examRepository.findById(examId).orElseThrow(() ->
            new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound")
        );
        if (exam.getUserLogin() != null){
            String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new BadRequestAlertException("User not logged in", "ExamStore", "userNotLoggedIn")
            );
            return Objects.equals(exam.getUserLogin(), currentUserLogin);
        }
        return super.hasPermission(exam.getGroupId(), authority);
    }
}
