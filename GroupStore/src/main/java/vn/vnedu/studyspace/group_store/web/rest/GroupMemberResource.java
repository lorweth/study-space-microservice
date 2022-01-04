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
import org.springframework.security.access.prepost.PreAuthorize;
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
     * {@code POST group-members/group/:group-id} : Request to join a group (for ).
     *
     * @param groupId the id of the group.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupDTO, or with status {@code 400 (Bad Request)} if the group has already an ID.
     * @throws URISyntaxException of the Location URI syntax is incorrect.
     */
    @PreAuthorize("@groupMemberSecurity.notExists(#groupId)")
    @PostMapping("group-members/group/{groupId}")
    public ResponseEntity<GroupMemberDTO> joinGroup(@PathVariable Long groupId) throws URISyntaxException {
        log.debug("REST request to join group");
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(userLogin.isEmpty()){
            throw new BadRequestAlertException(ENTITY_NAME, "User not logged in", "userNotLoggedIn");
        }

        GroupMemberDTO groupMemberDTO = groupMemberService.saveAsWaiting(groupId, userLogin.get());
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
    @PreAuthorize("@groupMemberSecurity.hasPermission(#groupMemberDTO.getGroup().getId(), 'ADMIN')")
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

        GroupMemberDTO result = groupMemberService.save(groupMemberDTO);
        kafkaService.storeGroupMember(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupMemberDTO.getId().toString()))
            .body(result);
    }

    /** ! Patch method will not send Group Data
     *
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
    @PreAuthorize("@groupMemberSecurity.hasPermissionOnGroupOfMember(#id, 'ADMIN')")
    @PatchMapping(value = "/group-members/{id}", consumes = { "application/json", "application/merge-patch+json" })
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

        Optional<GroupMemberDTO> result = groupMemberService.partialUpdate(groupMemberDTO);
        kafkaService.storeGroupMember(result.orElse(null)); // sync kafka
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET /group-members/group/:group-id} : get member data by current user login and groupId.
     *
     * @param groupId the id of the group.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/group-members/group/{groupId}")
    public ResponseEntity<GroupMemberDTO> getMemberData(@PathVariable Long groupId) {
        log.debug("REST request to get a Member Data by Current user login with groupId {}", groupId);
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("User not logged in", ENTITY_NAME, "userNotLoggedIn"));
        Optional<GroupMemberDTO> result = groupMemberService.findByUserLoginAndGroupId(currentUserLogin, groupId);
        return ResponseEntity.ok().body(result.orElse(null));
    }

    /**
     * {@code GET  /group-members/group/:groupId/admin} : get all the admin in group {id}.
     *
     * @param groupId the id of the group.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupMembers in body.
     */
    @GetMapping("/group-members/group/{groupId}/admin")
    public ResponseEntity<List<GroupMemberDTO>> getAllAdmin(@PathVariable Long groupId, Pageable pageable) {
        log.debug("REST request to get a page of GroupMembers");
        Page<GroupMemberDTO> page = groupMemberService.findAllAdminInGroup(groupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /group-members/group/:groupId/member} : get all the member in group {id}.
     *
     * @param groupId the id of the group.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupMembers in body.
     */
    @PreAuthorize("@groupMemberSecurity.hasPermission(#groupId, 'MEMBER')")
    @GetMapping("/group-members/group/{groupId}/member")
    public ResponseEntity<List<GroupMemberDTO>> getAllMembers(@PathVariable Long groupId, Pageable pageable) {
        log.debug("REST request to get a page of GroupMembers");
        Page<GroupMemberDTO> page = groupMemberService.findAllMemberInGroup(groupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /group-members/group/:groupId/waiting} : get all the waiting member in group {id}.
     *
     * @param groupId the id of the group.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupMembers in body.
     */
    @PreAuthorize("@groupMemberSecurity.hasPermission(#groupId, 'MEMBER')")
    @GetMapping("/group-members/group/{groupId}/waiting")
    public ResponseEntity<List<GroupMemberDTO>> getAllWaitingMember(@PathVariable Long groupId, Pageable pageable) {
        log.debug("REST request to get a page of GroupMembers");
        Page<GroupMemberDTO> page = groupMemberService.findAllWaitingInGroup(groupId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /group-members/username/:userLogin/group/:groupId} : get the member name "username" in group "groupId".
     *
     * @param userLogin the username of member.
     * @param groupId the id of the groupMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@groupMemberSecurity.hasPermission(#groupId, 'MEMBER')")
    @GetMapping("/group-members/username/{userLogin}/group/{groupId}")
    public ResponseEntity<GroupMemberDTO> getGroupMember(@PathVariable String userLogin, @PathVariable Long groupId) {
        log.debug("REST request to get GroupMember {} in Group {}", userLogin, groupId);
        Optional<GroupMemberDTO> groupMemberDTO = groupMemberService.findByUserLoginAndGroupId(userLogin, groupId);
        return ResponseUtil.wrapOrNotFound(groupMemberDTO);
    }

    /**
     * {@code DELETE  /group-members/:id} : delete the "id" groupMember.
     *
     * @param id the id of the groupMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("@groupMemberSecurity.hasPermissionOnGroupOfMember(#id, 'ADMIN')")
    @DeleteMapping("/group-members/{id}")
    public ResponseEntity<Void> deleteGroupMember(@PathVariable Long id) {
        log.debug("REST request to delete GroupMember : {}", id);
        groupMemberService.delete(id);
        kafkaService.deleteGroupMember(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
