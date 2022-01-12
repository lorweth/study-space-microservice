package vn.vnedu.studyspace.answer_store.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.security.SecurityUtils;
import vn.vnedu.studyspace.answer_store.service.WrongAnswerService;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerDTO;
import vn.vnedu.studyspace.answer_store.service.dto.SummaryResultDTO;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WrongAnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerSheetResource.class);

    private static final String ENTITY_NAME = "answerStoreAnswerSheet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WrongAnswerService wrongAnswerService;

    public WrongAnswerResource(WrongAnswerService wrongAnswerService) {
        this.wrongAnswerService = wrongAnswerService;
    }

    /**
     * {@code GET /answer-sheets/:id/wrong-answer}
     *
     * @param authorization header authorization.
     * @param id the id of the answerSheet.
     * @return the List of wrong answer.
     */
    @GetMapping("/answers/{id}/wrong-answer")
    public Mono<ResponseEntity<List<AnswerDTO>>> getWrongAnswer(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        log.debug("REST request to get all Wrong answer of AnswerSheet {}", id);
        return wrongAnswerService.findWrongAnswer(id, authorization)
            .collectList()
            .map(list -> ResponseEntity.ok().body(list));
    }

    @GetMapping("/answers/exam/{examId}/summary")
    public Mono<ResponseEntity<List<SummaryResultDTO>>> getSummaryOfExam(@RequestHeader("Authorization") String authorization, @PathVariable Long examId){
        log.debug("REST request to get summary of AnswerSheet {}", examId);

        return SecurityUtils.getCurrentUserLogin()
            .switchIfEmpty(Mono.error(new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn")))
            .flatMap(userLogin -> wrongAnswerService
                .getAllSummaryResultOfExam(examId, userLogin, authorization)
                .collectList()
            )
            .map(list -> ResponseEntity.ok().body(list));

    }

}
