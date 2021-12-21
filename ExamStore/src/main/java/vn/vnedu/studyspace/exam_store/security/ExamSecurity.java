package vn.vnedu.studyspace.exam_store.security;

import org.springframework.stereotype.Component;
import vn.vnedu.studyspace.exam_store.domain.Exam;
import vn.vnedu.studyspace.exam_store.repository.ExamRepository;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

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
        Optional<Exam> exam = examRepository.findById(examId);
        if(exam.isEmpty()){
            throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
        }
        return super.hasPermission(exam.get().getGroupId(), authority);
    }

    //    public boolean hasPermission(Long examId, String authority){
//        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
//        if(userLogin.isEmpty()){
//            throw new BadRequestAlertException("User not logged in", "ExamStore", "userNotLoggedIn");
//        }
//
//        // Find the "id" exam
//        Optional<Exam> exam = examRepository.findById(examId);
//        if(exam.isEmpty()){
//            throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
//        }
//
//        Optional<GroupMember> groupMember = groupMemberRepository.findByUserLoginAndGroupId(userLogin.get(), exam.get().getGroupId());
//        if (groupMember.isEmpty()) {
//            throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
//        }
//
//        switch (authority){
//            case "ADMIN":
//                return groupMember.get().getRole() == 2;
//            case "MEMBER":
//                return groupMember.get().getRole() >= 1;
//            default:
//                return false;
//        }
//    }
}
