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
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.security.SecurityUtils;
import vn.vnedu.studyspace.exam_store.service.GroupMemberService;
import vn.vnedu.studyspace.exam_store.service.QuestionGroupService;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionGroupDTO;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.exam_store.domain.QuestionGroup}.
 */
@RestController
@RequestMapping("/api")
public class QuestionGroupResource {

    private final Logger log = LoggerFactory.getLogger(QuestionGroupResource.class);

    private static final String ENTITY_NAME = "examStoreQuestionGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionGroupService questionGroupService;

    private final GroupMemberService groupMemberService;

    private final QuestionGroupRepository questionGroupRepository;

    public QuestionGroupResource(QuestionGroupService questionGroupService, QuestionGroupRepository questionGroupRepository, GroupMemberService groupMemberService) {
        this.questionGroupService = questionGroupService;
        this.questionGroupRepository = questionGroupRepository;
        this.groupMemberService = groupMemberService;
    }

    /**
     * {@code POST  /question-groups} : Create a new questionGroup for current user login.
     *
     * @param questionGroupDTO the questionGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionGroupDTO, or with status {@code 400 (Bad Request)} if the questionGroup has already an ID or if user not logged in.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/question-groups")
    public ResponseEntity<QuestionGroupDTO> createQuestionGroup(@Valid @RequestBody QuestionGroupDTO questionGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionGroup : {}", questionGroupDTO);
        if (questionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new questionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Check current user
        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        // set user login for question groups
        questionGroupDTO.setUserLogin(currentUserLoginOptional.get());

        QuestionGroupDTO result = questionGroupService.save(questionGroupDTO);
        return ResponseEntity
            .created(new URI("/api/question-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST /question-groups} : Create a new questionGroup for group.
     *
     * @param groupId the group to create questionGroup.
     * @param questionGroupDTO the questionGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionGroupDTO, or with status {@code 400 (Bad Request)} if the questionGroup has already an ID or if user not logged in or if user is not admin of group.
     * @throws URISyntaxException
     */
    @PostMapping("/question-groups/group/{group-id}")
    public ResponseEntity<QuestionGroupDTO> createQuestionGroupForGroup(@PathVariable("group-id") Long groupId, @Valid @RequestBody QuestionGroupDTO questionGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save QuestionGroup: {} for group {}", questionGroupDTO, groupId);
        if(questionGroupDTO.getId() !=null){
            throw new BadRequestAlertException("A new questionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Check current user
        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        // is admin of group?
        if (Boolean.FALSE.equals(groupMemberService.isGroupAdmin(groupId,currentUserLoginOptional.get()))){
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        // set groupId for questionGroup
        questionGroupDTO.setGroupId(groupId);

        QuestionGroupDTO result = questionGroupService.save(questionGroupDTO);
        return ResponseEntity
            .created(new URI("/api/question-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST /question-groups/save-all} : Create new QuestionGroup with question list.
     *
     * @param questionGroupDTO the list contains the id of the questions.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionGroupDTO, or with status {@code 400 (Bad Request)} if the questionGroup has already an ID or if user not logged in.
     * @throws URISyntaxException
     */
    @PostMapping("/question-groups/save-to-my-repo")
    public ResponseEntity<QuestionGroupDTO> saveExam(@Valid @RequestBody QuestionGroupDTO questionGroupDTO) throws URISyntaxException {
        log.debug("REST request to save Exam to QuestionGroup");
        if(questionIdList.isEmpty()){
            throw new BadRequestAlertException("The list is empty", ENTITY_NAME, "listEmpty");
        }

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        QuestionGroupDTO result = questionGroupService.
        return ResponseEntity
            .created(new URI("/api/question-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /question-groups/:id} : Updates an existing questionGroup.
     *
     * @param id the id of the questionGroupDTO to save.
     * @param questionGroupDTO the questionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/question-groups/{id}")
    public ResponseEntity<QuestionGroupDTO> updateQuestionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestionGroupDTO questionGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuestionGroup : {}, {}", id, questionGroupDTO);
        if (questionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        Optional<QuestionGroupDTO> prevQuestionGroup = questionGroupService.findOne(questionGroupDTO.getId());

        if (prevQuestionGroup.isEmpty()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // Check current user
        Optional<String> currentUserLoginOptional = SecurityUtils.getCurrentUserLogin();
        if (currentUserLoginOptional.isEmpty()){
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        // permission of current user
        if (
            Boolean.FALSE.equals(groupMemberService.isGroupAdmin(prevQuestionGroup.get().getGroupId(),currentUserLoginOptional.get()))
                && !Objects.equals(prevQuestionGroup.get().getUserLogin(), currentUserLoginOptional.get())
        ){
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        questionGroupDTO.setGroupId(prevQuestionGroup.get().getGroupId());
        questionGroupDTO.setUserLogin(prevQuestionGroup.get().getUserLogin());
        QuestionGroupDTO result = questionGroupService.save(questionGroupDTO);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /question-groups/:id} : Partial updates given fields of an existing questionGroup, field will ignore if it is null
     *
     * @param id the id of the questionGroupDTO to save.
     * @param questionGroupDTO the questionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the questionGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/question-groups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<QuestionGroupDTO> partialUpdateQuestionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuestionGroupDTO questionGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuestionGroup partially : {}, {}", id, questionGroupDTO);
        if (questionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionGroupDTO> result = questionGroupService.partialUpdate(questionGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /question-groups} : get all the questionGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionGroups in body.
     */
    @GetMapping("/question-groups")
    public ResponseEntity<List<QuestionGroupDTO>> getAllQuestionGroups(Pageable pageable) {
        log.debug("REST request to get a page of QuestionGroups");

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        Page<QuestionGroupDTO> page = questionGroupService.findAllByUserLogin(currentUserLogin.get(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /question-groups/:id} : get the "id" questionGroup.
     *
     * @param id the id of the questionGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/question-groups/{id}")
    public ResponseEntity<QuestionGroupDTO> getQuestionGroup(@PathVariable Long id) {
        log.debug("REST request to get QuestionGroup : {}", id);
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        Optional<QuestionGroupDTO> questionGroupDTOOptional = questionGroupService.findOne(id);
        if(questionGroupDTOOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if (
            Boolean.FALSE.equals(groupMemberService.isGroupMember(questionGroupDTOOptional.get().getGroupId(), currentUserLogin.get()))
            && Boolean.FALSE.equals(groupMemberService.isGroupAdmin(questionGroupDTOOptional.get().getGroupId(), currentUserLogin.get()))
            && !Objects.equals(questionGroupDTOOptional.get().getUserLogin(), currentUserLogin.get())
        ) {
            throw new BadRequestAlertException("Unauthorized access", ENTITY_NAME, "unauthorizedAccess");
        }
        return ResponseEntity.ok().body(questionGroupDTOOptional.get());
    }

    /**
     * {@code GET /question-groups/group/:groupId} : get all questionGroup contain "groupId".
     *
     * @param groupId the id of the group.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and the list of questionGroups in body.
     */
    @GetMapping("/question-groups/group/{groupId}")
    public ResponseEntity<List<QuestionGroupDTO>> getQuestionGroupByGroup(@PathVariable Long groupId, Pageable pageable) {
        log.debug("REST request to get QuestionGroup by Group: {}", groupId);

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if (
            Boolean.FALSE.equals(groupMemberService.isGroupMember(groupId, currentUserLogin.get()))
            && Boolean.FALSE.equals(groupMemberService.isGroupAdmin(groupId, currentUserLogin.get()))
        ) {
            throw new BadRequestAlertException("Unauthorized access", ENTITY_NAME, "unauthorizedAccess");
        }

        Page<QuestionGroupDTO> page = questionGroupService.findAllByGroupId(groupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /question-groups/:id} : delete the "id" questionGroup.
     *
     * @param id the id of the questionGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/question-groups/{id}")
    public ResponseEntity<Void> deleteQuestionGroup(@PathVariable Long id) {
        log.debug("REST request to delete QuestionGroup : {}", id);

        Optional<QuestionGroupDTO> questionGroup = questionGroupService.findOne(id);
        if(questionGroup.isEmpty()) {
            throw new BadRequestAlertException("QuestionGroup not exists", ENTITY_NAME, "questionGroupNotExists");
        }

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn");
        }

        if (
            Boolean.FALSE.equals(groupMemberService.isGroupAdmin(questionGroup.get().getGroupId(), currentUserLogin.get()))
            && Objects.equals(questionGroup.get().getUserLogin(), currentUserLogin.get())
        ) {
            throw new BadRequestAlertException("Unauthorized access", ENTITY_NAME, "unauthorizedAccess");
        }

        questionGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
