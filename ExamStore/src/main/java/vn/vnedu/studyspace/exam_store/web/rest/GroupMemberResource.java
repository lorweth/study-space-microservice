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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.exam_store.domain.GroupMember}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(GroupMemberResource.class);

    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberResource(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }

    /**
     * {@code GET  /group-members} : get all the groupMembers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupMembers in body.
     */
    @GetMapping("/group-members")
    public List<GroupMember> getAllGroupMembers() {
        log.debug("REST request to get all GroupMembers");
        return groupMemberRepository.findAll();
    }

    /**
     * {@code GET  /group-members/:id} : get the "id" groupMember.
     *
     * @param id the id of the groupMember to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupMember, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/group-members/{id}")
    public ResponseEntity<GroupMember> getGroupMember(@PathVariable Long id) {
        log.debug("REST request to get GroupMember : {}", id);
        Optional<GroupMember> groupMember = groupMemberRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(groupMember);
    }
}
