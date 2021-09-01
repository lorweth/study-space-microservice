package vn.vnedu.studyspace.exam_store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.service.GroupMemberService;
import vn.vnedu.studyspace.exam_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.exam_store.domain.GroupMember}.
 */
@RestController
@RequestMapping("/api")
public class GroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(GroupMemberResource.class);

    private final GroupMemberService groupMemberService;

    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberResource(GroupMemberService groupMemberService, GroupMemberRepository groupMemberRepository) {
        this.groupMemberService = groupMemberService;
        this.groupMemberRepository = groupMemberRepository;
    }

    /**
     * {@code GET  /group-members} : get all the groupMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupMembers in body.
     */
    @GetMapping("/group-members")
    public List<GroupMemberDTO> getAllGroupMembers() {
        log.debug("REST request to get all GroupMembers");
        return groupMemberService.findAll();
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
}
