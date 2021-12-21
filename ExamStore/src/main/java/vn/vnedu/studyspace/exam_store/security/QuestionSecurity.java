package vn.vnedu.studyspace.exam_store.security;

import org.springframework.stereotype.Component;
import vn.vnedu.studyspace.exam_store.domain.Question;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionRepository;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

@Component
public class QuestionSecurity extends QuestionGroupSecurity {

    private final QuestionRepository questionRepository;

    public QuestionSecurity(QuestionGroupRepository questionGroupRepository, GroupMemberRepository groupMemberRepository, QuestionRepository questionRepository) {
        super(questionGroupRepository, groupMemberRepository);
        this.questionRepository = questionRepository;
    }

    @Override
    public boolean hasPermission(Long questionId, String authority) {
        Optional<Question> question = questionRepository.findById(questionId);
        if(question.isEmpty()){
            throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
        }
        Long repoId = question.get().getQuestionGroup().getId();
        return super.hasPermission(repoId, authority);
    }
}
