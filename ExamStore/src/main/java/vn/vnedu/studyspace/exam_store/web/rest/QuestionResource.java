package vn.vnedu.studyspace.exam_store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.vnedu.studyspace.exam_store.repository.QuestionRepository;
import vn.vnedu.studyspace.exam_store.service.CorrectAnswerService;
import vn.vnedu.studyspace.exam_store.service.QuestionService;
import vn.vnedu.studyspace.exam_store.service.dto.CorrectAnswerDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.exam_store.domain.Question}.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "examStoreQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;

    private final CorrectAnswerService correctAnswerService;

    private final QuestionRepository questionRepository;

    public QuestionResource(QuestionService questionService, QuestionRepository questionRepository, CorrectAnswerService correctAnswerService) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.correctAnswerService = correctAnswerService;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param questionDTO the questionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionDTO, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@questionGroupSecurity.hasPermission(#questionDTO.getQuestionGroup().getId(), 'ADMIN')")
    @PostMapping("/questions")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity
            .created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@questionGroupSecurity.hasPermission(#questionDTO.getQuestionGroup().getId(), 'ADMIN')")
    @PutMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Question : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PatchMapping(value = "/questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public ResponseEntity<QuestionDTO> partialUpdateQuestion(
//        @PathVariable(value = "id", required = false) final Long id,
//        @NotNull @RequestBody QuestionDTO questionDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update Question partially : {}, {}", id, questionDTO);
//        if (questionDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, questionDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!questionRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<QuestionDTO> result = questionService.partialUpdate(questionDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//            result,
//            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString())
//        );
//    }

    /**
     * {@code GET  /questions/exam/:examId} : get all the questions in Exam.
     *
     * @param examId the id of the exam.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @PreAuthorize("@examSecurity.hasPermission(#examId, 'MEMBER')")
    @GetMapping("/questions/exam/{examId}")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@PathVariable Long examId) {
        log.debug("REST request to get a list of Questions in the Exam {}", examId);
        List<QuestionDTO> questions = questionService.findAllByExamId(examId);
        return ResponseEntity.ok().body(questions);
    }

    /**
     * {@code GET /questions/repo/:repoId} : get all question in repo "repoId".
     *
     * @param repoId the id of question group.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @PreAuthorize("@questionGroupSecurity.hasPermission(#repoId, 'MEMBER')")
    @GetMapping("/questions/repo/{repoId}")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsInRepo(@PathVariable Long repoId, Pageable pageable){
        log.debug("REST request to get all questions of questionGroup {}", repoId);
        Page<QuestionDTO> page = questionService.findAllByRepoId(repoId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@questionSecurity.hasPermission(#id, 'MEMBER')")
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        Optional<QuestionDTO> questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionDTO);
    }

    @GetMapping("questions/test-feign")
    public ResponseEntity<String> test(@RequestHeader Map<String, String> headers) {
        headers.forEach((k, v) -> log.debug("Header {}: {}", k, v));
        return ResponseEntity.ok().body("Test duoc roi ne");
    }

    /**
     * {@code GET /questions/correct-answer} : get a list of correct answer.
     *
     * @param questionIdList the list of questionId.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("questions/correct-answer")
    public ResponseEntity<List<CorrectAnswerDTO>> getAllCorrectAnswer(@RequestBody List<Long> questionIdList) {
        log.debug("REST request to get a list of correct answer");
        return ResponseEntity.ok().body(correctAnswerService.getCorrectAnswer(questionIdList));
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("@questionSecurity.hasPermission(#id, 'ADMIN')")
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionService.deleteWithOption(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
