package vn.vnedu.studyspace.group_store.web.rest;

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
import vn.vnedu.studyspace.group_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.group_store.security.SecurityUtils;
import vn.vnedu.studyspace.group_store.service.GroupMemberService;
import vn.vnedu.studyspace.group_store.service.GroupService;
import vn.vnedu.studyspace.group_store.service.KafkaService;
import vn.vnedu.studyspace.group_store.service.dto.GroupDTO;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.group_store.domain.GroupMember}.
 */
@RestController
@RequestMapping("/api")
public class GroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(GroupMemberResource.class);

    private static final String ENTITY_NAME = "groupStoreGroupMember";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupMemberService groupMemberService;

    private final GroupMemberRepository groupMemberRepository;

    private final GroupService groupService;

    private final KafkaService kafkaService;

    public GroupMemberResource(GroupMemberService groupMemberService, GroupMemberRepository groupMemberRepository, GroupService groupService, KafkaService kafkaService) {
        this.groupMemberService = groupMemberService;
        this.groupMemberRepository = groupMemberRepository;
        this.groupService = groupService;
        this.kafkaService = kafkaService;
    }

    /**
     * {@code POST group-members/group-id/:groupId} : Create new member for group "groupId" by current user.
     *
     * @param groupId the id of the group.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupDTO, or with status {@code 400 (Bad Request)} if the group has already an ID.
     * @throws URISyntaxException of the Location URI syntax is incorrect.
     */
    @PostMapping("group-members/group-id/{group-id}")
    public ResponseEntity<GroupMemberDTO> joinGroup(@PathVariable("group-id") Long groupId) throws URISyntaxException {
        log.debug("REST request to join group");
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User is not logged in", ENTITY_NAME, "userIsNotLoggedIn");
        }
        Optional<GroupDTO> groupDTO = groupService.findOne(groupId);
        if(groupDTO.isEmpty()){
            throw new BadRequestAlertException("Group is not available", ENTITY_NAME, "groupIsNotAvailable");
        }
        Integer roleOfUser = groupMemberService.checkRole(currentUserLogin.get(), groupId);
        if(roleOfUser != -1){
            throw new BadRequestAlertException("User is member of the group", ENTITY_NAME, "userIsMemberOfGroup");
        }
        GroupMemberDTO groupMemberDTO = groupMemberService.saveWaitingMember(groupDTO.get(), currentUserLogin.get());
        // send message update groupMember to other microservice.
        kafkaService.storeGroupMember(groupMemberDTO);
        return ResponseEntity
            .created(new URI("/api/group-members" + groupMemberDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "GroupMember", groupMemberDTO.getId().toString()))
            .body(groupMemberDTO);
    }

    /**
     * {@code PUT  /group-members/:id} : Updates an existing groupMember.
     *
     * @param id the id of the groupMemberDTO to save.
     * @param groupMemberDTO the groupMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupMemberDTO,
     * or with status {@code 400 (Bad Request)} if the groupMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/group-members/{id}")
    public ResponseEntity<GroupMemberDTO> updateGroupMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GroupMemberDTO groupMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GroupMember : {}, {}", id, groupMemberDTO);
        if (groupMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User is not logged in", ENTITY_NAME, "userIsNotLoggedIn");
        }

        if(!groupMemberService.isAdmin(currentUserLogin.get(), groupMemberDTO.getGroup().getId())) {
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        GroupMemberDTO result = groupMemberService.save(groupMemberDTO);
        // send message update groupMember to other microservices.
        kafkaService.storeGroupMember(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /group-members/:id} : Partial updates given fields of an existing groupMember, field will ignore if it is null
     *
     * @param id the id of the groupMemberDTO to save.
     * @param groupMemberDTO the groupMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupMemberDTO,
     * or with status {@code 400 (Bad Request)} if the groupMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/group-members/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GroupMemberDTO> partialUpdateGroupMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GroupMemberDTO groupMemberDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GroupMember partially : {}, {}", id, groupMemberDTO);
        if (groupMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()){
            throw new BadRequestAlertException("User is not logged in", ENTITY_NAME, "userIsNotLoggedIn");
        }

        if(!groupMemberService.isAdmin(currentUserLogin.get(), groupMemberDTO.getGroup().getId())) {
            throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
        }

        Optional<GroupMemberDTO> result = groupMemberService.partialUpdate(groupMemberDTO);
        // Send message to update groupMember in other microservices.
        result.ifPresent(kafkaService::storeGroupMember);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /group-members} : get all the groupMembers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupMembers in body.
     */
    @GetMapping("/group-members")
    public ResponseEntity<List<GroupMemberDTO>> getAllGroupMembers(Pageable pageable) {
        log.debug("REST request to get a page of GroupMembers");
        Page<GroupMemberDTO> page = groupMemberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /group-members/:id} : get the "id" groupMember.
     *
     * @param id the id of the groupMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/group-members/{id}")
    public ResponseEntity<GroupMemberDTO> getGroupMember(@PathVariable Long id) {
        log.debug("REST request to get GroupMember : {}", id);
        Optional<GroupMemberDTO> groupMemberDTO = groupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupMemberDTO);
    }

    /**
     * {@code GET /group-members/group-id/:group-id} : get all member of "group-id" group.
     *
     * @param groupId the id of the group.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and the list of groupMembers in body.
     */
    @GetMapping("group-members/group-id/{group-id}")
    public ResponseEntity<List<GroupMemberDTO>> getAllMemberOfGroup(@PathVariable("group-id") Long groupId, Pageable pageable) {
        log.debug("REST request get all member of group: {}", groupId);
        Page<GroupMemberDTO> page = groupMemberService.findAllByGroupId(groupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity
            .ok()
            .headers(headers)
            .body(page.getContent());
    }

    /**
     * {@code GET group-members/group-id/{group-id}/username/{user-name}} : get all member by groupId and userLogin.
     *
     * @param groupId the id of the group.
     * @param userName full of a part of userLogin.
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and the list of groupMembers in body.
     */
    @GetMapping("group-members/group-id/{group-id}/username/{user-name}")
    public ResponseEntity<List<GroupMemberDTO>> getAllMemberByGroupIdAndUserLogin(@PathVariable("group-id") Long groupId, @PathVariable("user-name") String userName) {
        log.debug("REST request get all member by groupId: {} and name: {}", groupId, userName);
        Optional<GroupDTO> selectedGroup = groupService.findOne(groupId);
        if(selectedGroup.isEmpty()){
            throw new BadRequestAlertException("Group not found", ENTITY_NAME, "groupNotFound");
        }
        List<GroupMemberDTO> list = groupMemberService.findByGroupAndUserLoginContainingIgnoreCase(selectedGroup.get(), userName);
        return ResponseEntity
            .ok()
            .body(list);
    }

    /**
     * {@code DELETE /group-members/:id} : delete the "id" groupMember when current user "cancel join request" or admin "remove member".
     *
     * @param id the id of the groupMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/group-members/{id}")
    public ResponseEntity<Void> deleteGroupMember(@PathVariable Long id, @RequestBody GroupMemberDTO groupMemberDTO){
        log.debug("REST request to delete GroupMember : {}", id);
        if(!id.equals(groupMemberDTO.getId())) {
            throw new BadRequestAlertException("Id not invalid", ENTITY_NAME, "idNotInvalid");
        }
        // Request user logged in
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if(currentUserLogin.isEmpty()) {
            throw new BadRequestAlertException("User is not logged in", ENTITY_NAME, "userIsNotLoggedIn");
        }

        if(!groupMemberService.isAdmin(currentUserLogin.get(), groupMemberDTO.getGroup().getId())) {
            if(currentUserLogin.get().equals(groupMemberDTO.getUserLogin())){
                groupMemberService.delete(id);
                // send message delete groupMember in other microservice
                kafkaService.deleteGroupMember(id);
            }
            else {
                throw new BadRequestAlertException("Full authentication for this action", ENTITY_NAME, "fullAuthenticationForThisAction");
            }
        } else {
            groupMemberService.delete(id);
            // send message delete groupMember in other microservice
            kafkaService.deleteGroupMember(id);
        }

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
