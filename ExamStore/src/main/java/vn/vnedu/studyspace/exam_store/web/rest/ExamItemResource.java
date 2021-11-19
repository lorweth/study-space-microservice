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
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.security.SecurityUtils;
import vn.vnedu.studyspace.exam_store.service.ExamItemService;
import vn.vnedu.studyspace.exam_store.service.ExamService;
import vn.vnedu.studyspace.exam_store.service.GroupMemberService;
import vn.vnedu.studyspace.exam_store.service.QuestionService;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.exam_store.domain.ExamItem}.
 */
@RestController
@RequestMapping("/api")
public class ExamItemResource {

    private final Logger log = LoggerFactory.getLogger(ExamItemResource.class);

    private static final String ENTITY_NAME = "examStoreExamItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamItemService examItemService;

    private final ExamItemRepository examItemRepository;

    private final GroupMemberService groupMemberService;

    private final ExamService examService;

    private final QuestionService questionService;

    public ExamItemResource(ExamItemService examItemService, ExamItemRepository examItemRepository, GroupMemberService groupMemberService, ExamService examService, QuestionService questionService) {
        this.examItemService = examItemService;
        this.examItemRepository = examItemRepository;
        this.groupMemberService = groupMemberService;
        this.examService = examService;
        this.questionService = questionService;
    }

    /**
     * {@code POST  /exam-items} : Create a new examItem.
     *
     * @param examItemDTO the examItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examItemDTO, or with status {@code 400 (Bad Request)} if the examItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-items")
    public ResponseEntity<ExamItemDTO> createExamItem(@Valid @RequestBody ExamItemDTO examItemDTO) throws URISyntaxException {
        log.debug("REST request to save ExamItem : {}", examItemDTO);
        if (examItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new examItem cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (Objects.isNull(examItemDTO.getExam().getId())) {
            throw new BadRequestAlertException("Exam not found", ENTITY_NAME, "examNotFound");
        }
        if (Objects.isNull(examItemDTO.getRepo().getId())) {
            throw new BadRequestAlertException("Repo not found", ENTITY_NAME, "repoNotFound");
        }

        long examId = examItemDTO.getExam().getId();
        long repoId = examItemDTO.getRepo().getId();

        Optional<ExamDTO> currentExamDTO = examService.findOne(examId);
        if (currentExamDTO.isEmpty()){
            throw new BadRequestAlertException("Exam not exists", ENTITY_NAME, "examNotExists");
        }

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if (Boolean.FALSE.equals(groupMemberService.isGroupAdmin(currentExamDTO.get().getGroupId(), currentUserLoginOptional.get()))) {
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        long total = questionService.countByRepo(repoId);
        if (total < examItemDTO.getNumOfQuestion()) {
            throw new BadRequestAlertException("Out of questions number", ENTITY_NAME, "outOfNumber");
        }

        ExamItemDTO result = examItemService.save(examItemDTO);
        return ResponseEntity
            .created(new URI("/api/exam-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-items/:id} : Updates an existing examItem.
     *
     * @param id the id of the examItemDTO to save.
     * @param examItemDTO the examItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examItemDTO,
     * or with status {@code 400 (Bad Request)} if the examItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-items/{id}")
    public ResponseEntity<ExamItemDTO> updateExamItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamItemDTO examItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExamItem : {}, {}", id, examItemDTO);
        if (examItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (Objects.isNull(examItemDTO.getExam().getId())) {
            throw new BadRequestAlertException("Exam not found", ENTITY_NAME, "examNotFound");
        }
        if (Objects.isNull(examItemDTO.getRepo().getId())) {
            throw new BadRequestAlertException("Repo not found", ENTITY_NAME, "repoNotFound");
        }

        long examId = examItemDTO.getExam().getId();
        long repoId = examItemDTO.getRepo().getId();

        Optional<ExamDTO> currentExamDTO = examService.findOne(examId);
        if (currentExamDTO.isEmpty()){
            throw new BadRequestAlertException("Exam not exists", ENTITY_NAME, "examNotExists");
        }

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if (Boolean.FALSE.equals(groupMemberService.isGroupAdmin(currentExamDTO.get().getGroupId(), currentUserLoginOptional.get()))) {
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        long total = questionService.countByRepo(repoId);
        if (total < examItemDTO.getNumOfQuestion()) {
            throw new BadRequestAlertException("Out of questions number", ENTITY_NAME, "outOfNumber");
        }

        ExamItemDTO result = examItemService.save(examItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-items/:id} : Partial updates given fields of an existing examItem, field will ignore if it is null
     *
     * @param id the id of the examItemDTO to save.
     * @param examItemDTO the examItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examItemDTO,
     * or with status {@code 400 (Bad Request)} if the examItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExamItemDTO> partialUpdateExamItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamItemDTO examItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamItem partially : {}, {}", id, examItemDTO);
        if (examItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamItemDTO> result = examItemService.partialUpdate(examItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-items} : get all the examItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examItems in body.
     */
    @GetMapping("/exam-items")
    public ResponseEntity<List<ExamItemDTO>> getAllExamItems(Pageable pageable) {
        log.debug("REST request to get a page of ExamItems");
        Page<ExamItemDTO> page = examItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET / exam-items/exam/:examId} : Get all the examItems by Exam.
     *
     * @param examId the id of the exam.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examItems in body.
     */
    @GetMapping("/exam-items/exam/{examId}")
    public ResponseEntity<List<ExamItemDTO>> getExamItemsByExam(@PathVariable Long examId, Pageable pageable) {
        log.debug("REST request to get a page of ExamItems by Exam: {}", examId);

        Optional<ExamDTO> currentExamDTO = examService.findOne(examId);
        if (currentExamDTO.isEmpty()){
            throw new BadRequestAlertException("Exam not exists", ENTITY_NAME, "examNotExists");
        }

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if (Boolean.FALSE.equals(groupMemberService.isGroupAdmin(currentExamDTO.get().getGroupId(), currentUserLoginOptional.get()))) {
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }
        Page<ExamItemDTO> page = examItemService.findByExam(examId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-items/:id} : get the "id" examItem.
     *
     * @param id the id of the examItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-items/{id}")
    public ResponseEntity<ExamItemDTO> getExamItem(@PathVariable Long id) {
        log.debug("REST request to get ExamItem : {}", id);
        Optional<ExamItemDTO> examItemDTO = examItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examItemDTO);
    }

    /**
     * {@code DELETE  /exam-items/:id} : delete the "id" examItem.
     *
     * @param id the id of the examItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-items/{id}")
    public ResponseEntity<Void> deleteExamItem(@PathVariable Long id) {
        log.debug("REST request to delete ExamItem : {}", id);
        Optional<ExamItemDTO> currentExamItem = examItemService.findOne(id);
        if (currentExamItem.isEmpty()) {
            throw new BadRequestAlertException("ExamItem not exitsts", ENTITY_NAME, "examItemNotExists");
        }

        if (Objects.isNull(currentExamItem.get().getExam().getId())) {
            throw new BadRequestAlertException("Exam not found", ENTITY_NAME, "examNotFound");
        }

        Optional<ExamDTO> currentExamDTO = examService.findOne(currentExamItem.get().getExam().getId());
        if (currentExamDTO.isEmpty()){
            throw new BadRequestAlertException("Exam not exists", ENTITY_NAME, "examNotExists");
        }

        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if (Boolean.FALSE.equals(groupMemberService.isGroupAdmin(currentExamDTO.get().getGroupId(), currentUserLoginOptional.get()))) {
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }
        examItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
