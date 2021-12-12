package vn.vnedu.studyspace.exam_store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;
import vn.vnedu.studyspace.exam_store.repository.QuestionRepository;
import vn.vnedu.studyspace.exam_store.security.SecurityUtils;
import vn.vnedu.studyspace.exam_store.service.ExamService;
import vn.vnedu.studyspace.exam_store.service.GroupMemberService;
import vn.vnedu.studyspace.exam_store.service.QuestionGroupService;
import vn.vnedu.studyspace.exam_store.service.QuestionService;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionGroupDTO;
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

    private final GroupMemberService groupMemberService;

    private final ExamService examService;

    private final QuestionGroupService questionGroupService;

    private final QuestionRepository questionRepository;

    public QuestionResource(QuestionService questionService, QuestionRepository questionRepository, GroupMemberService groupMemberService, QuestionGroupService questionGroupService, ExamService examService) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.groupMemberService = groupMemberService;
        this.questionGroupService = questionGroupService;
        this.examService = examService;
    }

    /**
     * {@code POST  /questions} : Create a new question with list Option.
     *
     * @param questionDTO the questionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionDTO, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (Objects.isNull(questionDTO.getRepo())) {
            throw new BadRequestAlertException("A new Question must be created in a QuestionGroup", ENTITY_NAME, "questionGroupNotFound");
        }

        Optional<QuestionGroupDTO> currentQuestionGroupDTO = questionGroupService.findOne(questionDTO.getRepo().getId());
        if(currentQuestionGroupDTO.isEmpty()){
            throw new BadRequestAlertException("QuestionGroup: "+questionDTO.getRepo().getId()+" not exists", ENTITY_NAME, "questionGroupNotExists");
        }

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if(currentUserLoginOptional.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if(Boolean.FALSE.equals(groupMemberService.isGroupAdmin(currentQuestionGroupDTO.get().getGroupId(), currentUserLoginOptional.get()))
            && !Objects.equals(currentQuestionGroupDTO.get().getUserLogin(), currentUserLoginOptional.get())
        ) {
            throw new BadRequestAlertException("Full Authenticate for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        QuestionDTO result = questionService.saveWithListOption(questionDTO);
        return ResponseEntity
            .created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing question and option.
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

        if (Objects.isNull(questionDTO.getRepo())) {
            throw new BadRequestAlertException("A Question must be in a QuestionGroup", ENTITY_NAME, "questionGroupNotFound");
        }
        Optional<QuestionGroupDTO> currentQuestionGroupDTO = questionGroupService.findOne(questionDTO.getRepo().getId());
        if (currentQuestionGroupDTO.isEmpty()) {
            throw new BadRequestAlertException("QuestionGroup: "+questionDTO.getRepo().getId()+" not exists", ENTITY_NAME, "questionGroupNotExists");
        }

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if(
            Boolean.FALSE.equals(groupMemberService.isGroupAdmin(currentQuestionGroupDTO.get().getGroupId(), currentUserLoginOptional.get()))
            && !Objects.equals(currentQuestionGroupDTO.get().getUserLogin(), currentUserLoginOptional.get())
        ) {
            throw new BadRequestAlertException("Full Authenticate for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        QuestionDTO result = questionService.saveWithListOption(questionDTO);
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
    @PatchMapping(value = "/questions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<QuestionDTO> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Question partially : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionDTO> result = questionService.partialUpdate(questionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(Pageable pageable) {
        log.debug("REST request to get a page of Questions");
        Page<QuestionDTO> page = questionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        Optional<QuestionDTO> questionDTO = questionService.findOneWithOption(id);
        if (questionDTO.isEmpty()) {
            throw new BadRequestAlertException("Question not Exists", ENTITY_NAME, "questionNotExists");
        }

        long questionGroupId = questionDTO.get().getRepo().getId();
        Optional<QuestionGroupDTO> questionGroupDTO = questionGroupService.findOne(questionGroupId);
        if (questionGroupDTO.isEmpty()) {
            throw new BadRequestAlertException("QuestionGroup not Exists", ENTITY_NAME, "questionGroupNotExists");
        }

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if(
            Boolean.FALSE.equals(groupMemberService.isGroupMember(questionGroupDTO.get().getGroupId(), currentUserLogin.get()))
            && Boolean.FALSE.equals(groupMemberService.isGroupAdmin(questionGroupDTO.get().getGroupId(), currentUserLogin.get()))
            && !Objects.equals(questionGroupDTO.get().getUserLogin(), currentUserLogin.get())
        ) {
            throw new BadRequestAlertException("Unauthorized access", ENTITY_NAME, "unauthorizedAccess");
        }

        return ResponseEntity.ok().body(questionDTO.get());
    }

    /**
     * {@code GET /questions/repo/:questionGroupId} : Get all the question by questionGroup.
     * @param questionGroupId the id of QuestionGroup.
     * @param pageable pagination information.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and the list of questions in body.
     */
    @GetMapping("/questions/repo/{questionGroupId}")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsByRepo(@PathVariable Long questionGroupId, Pageable pageable) {
        log.debug("REST request get all Questions by QuestionGroup");

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        Optional<QuestionGroupDTO> questionGroupDTO = questionGroupService.findOne(questionGroupId);
        if(questionGroupDTO.isEmpty()) {
            throw new BadRequestAlertException("QuestionGroup not exists", ENTITY_NAME, "entityNotFound");
        }

        if(
            Boolean.FALSE.equals(groupMemberService.isGroupMember(questionGroupDTO.get().getGroupId(), currentUserLogin.get()))
            && Boolean.FALSE.equals(groupMemberService.isGroupAdmin(questionGroupDTO.get().getGroupId(), currentUserLogin.get()))
            && !Objects.equals(questionGroupDTO.get().getUserLogin(), currentUserLogin.get())
        ){
            throw new BadRequestAlertException("Unauthorized access", ENTITY_NAME, "unauthorizedAccess");
        }

        Page<QuestionDTO> page = questionService.findByRepo(questionGroupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /questions/exam/:examId} : Get all question of the exam.
     *
     * @param examId the id of the exam.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and the list of questions in body.
     */
    @GetMapping("/questions/exam/{examId}")
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsByExam(@PathVariable Long examId) {
        log.debug("REST request get all Questions by Exam");

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        Optional<ExamDTO> examDTO = examService.findOne(examId);
        if(examDTO.isEmpty()) {
            throw new BadRequestAlertException("Exam not exists", ENTITY_NAME, "examNotExists");
        }

        if(
            Boolean.FALSE.equals(groupMemberService.isGroupMember(examDTO.get().getGroupId(), currentUserLogin.get()))
            && Boolean.FALSE.equals(groupMemberService.isGroupAdmin(examDTO.get().getGroupId(), currentUserLogin.get()))
        ){
            throw new BadRequestAlertException("Unauthorized access", ENTITY_NAME, "unauthorizedAccess");
        }

        List<QuestionDTO> questionList = examService.getQuestions(examId);

        return ResponseEntity.ok().body(questionList);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
