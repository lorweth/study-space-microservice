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
import vn.vnedu.studyspace.group_store.repository.GroupRepository;
import vn.vnedu.studyspace.group_store.security.SecurityUtils;
import vn.vnedu.studyspace.group_store.service.GroupMemberService;
import vn.vnedu.studyspace.group_store.service.GroupService;
import vn.vnedu.studyspace.group_store.service.KafkaService;
import vn.vnedu.studyspace.group_store.service.dto.GroupDTO;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.group_store.domain.Group}.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "groupStoreGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupService groupService;

    private final GroupRepository groupRepository;

    private final GroupMemberService groupMemberService;

    private final KafkaService kafkaService;

    public GroupResource(GroupService groupService, GroupRepository groupRepository, GroupMemberService groupMemberService, KafkaService kafkaService) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.groupMemberService = groupMemberService;
        this.kafkaService = kafkaService;
    }

    /**
     * {@code POST  /groups} : Create a new group.
     *
     * @param groupDTO the groupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupDTO, or with status {@code 400 (Bad Request)} if the group has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/groups")
    public ResponseEntity<GroupDTO> createGroup(@Valid @RequestBody GroupDTO groupDTO) throws URISyntaxException {
        log.debug("REST request to save Group : {}", groupDTO);
        if (groupDTO.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<String> userLoginOptional = SecurityUtils.getCurrentUserLogin();
        if(userLoginOptional.isEmpty()){
            throw new BadRequestAlertException(ENTITY_NAME, "User not logged in", "userNotLoggedIn");
        }
        GroupDTO result = groupService.save(groupDTO);
        // Create new member as Admin
        GroupMemberDTO groupMemberDTO = groupMemberService.saveAsAdmin(result.getId(), userLoginOptional.get());
        // Sync with kafka
        kafkaService.storeGroupMember(groupMemberDTO);
        return ResponseEntity
            .created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groups/:id} : Updates an existing group.
     *
     * @param id the id of the groupDTO to save.
     * @param groupDTO the groupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupDTO,
     * or with status {@code 400 (Bad Request)} if the groupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@groupMemberSecurity.hasPermission(#id, 'ADMIN')")
    @PutMapping("/groups/{id}")
    public ResponseEntity<GroupDTO> updateGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GroupDTO groupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Group : {}, {}", id, groupDTO);
        if (groupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GroupDTO result = groupService.save(groupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /groups/:id} : Partial updates given fields of an existing group, field will ignore if it is null
     *
     * @param id the id of the groupDTO to save.
     * @param groupDTO the groupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupDTO,
     * or with status {@code 400 (Bad Request)} if the groupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@groupMemberSecurity.hasPermission(#id, 'ADMIN')")
    @PatchMapping(value = "/groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GroupDTO> partialUpdateGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GroupDTO groupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Group partially : {}, {}", id, groupDTO);
        if (groupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GroupDTO> result = groupService.partialUpdate(groupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
//    @GetMapping("/groups")
//    public ResponseEntity<List<GroupDTO>> getAllGroups(Pageable pageable) {
//        log.debug("REST request to get a page of Groups");
//        Page<GroupDTO> page = groupService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }

    /**
     * {@code GET /groups/name/:name} : get all the groups which name containing "name".
     *
     * @param name the name to retrieve.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("/groups/name/{name}")
    public ResponseEntity<List<GroupDTO>> getAllGroupByName(@PathVariable String name, Pageable pageable) {
        log.debug("REST request to get a page of Groups which name containing {}", name);
        Page<GroupDTO> page = groupService.findAllByNameContaining(name, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /groups/:id} : get the "id" group.
     *
     * @param id the id of the groupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);
        Optional<GroupDTO> groupDTO = groupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupDTO);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" group.
     *
     * @param id the id of the groupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
//    @PreAuthorize("@groupMemberSecurity.hasPermission(#id, 'ADMIN')")
//    @DeleteMapping("/groups/{id}")
//    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
//        log.debug("REST request to delete Group : {}", id);
//        Optional<Long> numOfMember = groupMemberService.countByGroupId(id);
//        if (numOfMember.isPresent() && numOfMember.get() > 1){
//            throw new BadRequestAlertException(ENTITY_NAME, "Cannot delete the Group", "cannotDeleteGroup");
//        }
//        groupService.delete(id);
//        return ResponseEntity
//            .noContent()
//            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//            .build();
//    }
}
