package vn.vnedu.studyspace.answer_store.security;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;
import vn.vnedu.studyspace.answer_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.answer_store.repository.GroupTimeTableRepository;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

@Component
public class GroupTimeTableSecurity extends GroupMemberSecurity{

    private final GroupTimeTableRepository groupTimeTableRepository;

    public GroupTimeTableSecurity(GroupMemberRepository groupMemberRepository, GroupTimeTableRepository groupTimeTableRepository) {
        super(groupMemberRepository);
        this.groupTimeTableRepository = groupTimeTableRepository;
    }

    @Override
    public Boolean hasPermission(Long groupTimeTableId, String authority) {
        Mono<GroupTimeTable> timeTable = groupTimeTableRepository.findById(groupTimeTableId);
//        return super.hasPermission(groupId, authority);
        return groupTimeTableRepository
            .findById(groupTimeTableId)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
            .map(groupTimeTable -> super.hasPermission(groupTimeTable.getGroupId(), authority));
    }
}
