package vn.vnedu.studyspace.answer_store.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.answer_store.repository.GroupTimeTableRepository;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

@Component
public class GroupTimeTableSecurity extends GroupMemberSecurity{

    private final Logger log = LoggerFactory.getLogger(GroupTimeTableSecurity.class);

    private final GroupTimeTableRepository groupTimeTableRepository;

    public GroupTimeTableSecurity(GroupMemberRepository groupMemberRepository, GroupTimeTableRepository groupTimeTableRepository) {
        super(groupMemberRepository);
        this.groupTimeTableRepository = groupTimeTableRepository;
    }

    @Override
    public Boolean hasPermission(Long groupTimeTableId, String authority) {
        log.debug("Check authorize {} to call request with groupTimeTable {}", authority, groupTimeTableId);
        return groupTimeTableRepository
            .findById(groupTimeTableId)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
            .flatMap(groupTimeTable -> super.compareRole(groupTimeTable.getGroupId(), authority))
            .block();
    }
}
