package vn.vnedu.studyspace.exam_store.security;

import org.springframework.stereotype.Component;
import vn.vnedu.studyspace.exam_store.domain.ExamItem;
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.repository.ExamRepository;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;
import java.util.Optional;

@Component
public class ExamItemSecurity extends ExamSecurity{

    private final ExamItemRepository examItemRepository;

    private final QuestionGroupRepository questionGroupRepository;

    public ExamItemSecurity(GroupMemberRepository groupMemberRepository, ExamRepository examRepository, ExamItemRepository examItemRepository, QuestionGroupRepository questionGroupRepository) {
        super(groupMemberRepository, examRepository);
        this.examItemRepository = examItemRepository;
        this.questionGroupRepository = questionGroupRepository;
    }

    @Override
    public boolean hasPermission(Long itemId, String authority) {
        Optional<ExamItem> item = examItemRepository.findById(itemId);
        if(item.isEmpty()){
            throw new BadRequestAlertException("Entity not found", "ExamStore", "entityNotFound");
        }
        return super.hasPermission(item.get().getExam().getId(), authority);
    }
}
