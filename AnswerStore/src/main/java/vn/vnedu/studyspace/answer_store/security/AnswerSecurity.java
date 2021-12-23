package vn.vnedu.studyspace.answer_store.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetItemRepository;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetRepository;
import vn.vnedu.studyspace.answer_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.answer_store.repository.GroupTimeTableRepository;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

import java.util.Objects;

@Component
public class AnswerSecurity extends GroupMemberSecurity{

    private final Logger log = LoggerFactory.getLogger(AnswerSecurity.class);

    private final AnswerSheetRepository answerSheetRepository;

    private final AnswerSheetItemRepository itemRepository;

    private final GroupTimeTableRepository groupTimeTableRepository;

    public AnswerSecurity(
        GroupMemberRepository groupMemberRepository,
        AnswerSheetRepository answerSheetRepository,
        GroupTimeTableRepository groupTimeTableRepository,
        AnswerSheetItemRepository itemRepository
    ) {
        super(groupMemberRepository);
        this.answerSheetRepository = answerSheetRepository;
        this.groupTimeTableRepository = groupTimeTableRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Boolean hasPermission(Long sheetId, String authority) {
        // Check userLogin get userLogin
        // Check AnswerSheetId get GroupTimeTableId
        // Check GroupTimeTable get GroupId
        // Check permission of GroupMember use compareRole
        return SecurityUtils
            .getCurrentUserLogin()
            .switchIfEmpty(Mono.error(new BadRequestAlertException("User not logged in", "AnswerStore", "userNotLoggedIn")))
            .flatMap(currentUserLogin -> answerSheetRepository
                .findById(sheetId)
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
                .flatMap(sheet -> {
                    if(Objects.equals(sheet.getUserLogin(), currentUserLogin)){
                        return Mono.error(new BadRequestAlertException("User not have permission", "AnswerStore", "userNotPermission"));
                    }
                    return Mono.just(sheet.getGroupTimeTable().getId());
                })
            )
            .flatMap(groupTimeTableRepository::findById)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
            .map(GroupTimeTable::getGroupId)
            .flatMap(groupId -> super.compareRole(groupId, authority))
            .block();
    }

    /**
     * Check permission to get of current user.
     *
     * @param sheetId id of AnswerSheet.
     * @return true if user has permission or else false.
     */
    public Boolean hasPermissionToGet(Long sheetId) {
        return SecurityUtils
            .getCurrentUserLogin()
            .switchIfEmpty(Mono.error(new BadRequestAlertException("User not logged in", "AnswerStore", "userNotLoggedIn")))
            .flatMap(currentUserLogin -> answerSheetRepository
                .findById(sheetId)
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
                .map(sheet -> Objects.equals(sheet.getUserLogin(), currentUserLogin))
            )
            .block();
    }

    /**
     * Check permission to choose an answer in the exam.
     *
     * @param itemId id of examItem
     * @return true if user has permission or else false.
     */
    public Boolean hasPermissionToChoose(Long itemId) {
        return SecurityUtils
            .getCurrentUserLogin()
            .switchIfEmpty(Mono.error(new BadRequestAlertException("User not logged in", "AnswerStore", "userNotLoggedIn")))
            .flatMap(currentUserLogin -> itemRepository
                .findById(itemId)
                .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
                .map(AnswerSheetItem::getAnswerSheetId)
                .flatMap(answerSheetRepository::findById)
                .map(AnswerSheet::getUserLogin)
                .map(userLogin -> Objects.equals(userLogin, currentUserLogin))
            )
            .block();
    }

}
