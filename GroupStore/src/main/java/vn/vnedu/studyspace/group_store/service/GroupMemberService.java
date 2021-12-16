package vn.vnedu.studyspace.group_store.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.group_store.domain.GroupMember;
import vn.vnedu.studyspace.group_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.service.mapper.GroupMemberMapper;

/**
 * Service Implementation for managing {@link GroupMember}.
 */
@Service
@Transactional
public class GroupMemberService {

    private final Logger log = LoggerFactory.getLogger(GroupMemberService.class);

    private final GroupMemberRepository groupMemberRepository;

    private final GroupMemberMapper groupMemberMapper;

    public GroupMemberService(GroupMemberRepository groupMemberRepository, GroupMemberMapper groupMemberMapper) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupMemberMapper = groupMemberMapper;
    }

    /**
     * Save a groupMember.
     *
     * @param groupMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public GroupMemberDTO save(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to save GroupMember : {}", groupMemberDTO);
        GroupMember groupMember = groupMemberMapper.toEntity(groupMemberDTO);
        groupMember = groupMemberRepository.save(groupMember);
        return groupMemberMapper.toDto(groupMember);
    }

    /**
     * Partially update a groupMember.
     *
     * @param groupMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GroupMemberDTO> partialUpdate(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to partially update GroupMember : {}", groupMemberDTO);

        return groupMemberRepository
            .findById(groupMemberDTO.getId())
            .map(existingGroupMember -> {
                groupMemberMapper.partialUpdate(existingGroupMember, groupMemberDTO);

                return existingGroupMember;
            })
            .map(groupMemberRepository::save)
            .map(groupMemberMapper::toDto);
    }

    /**
     * Get all the groupMembers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GroupMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GroupMembers");
        return groupMemberRepository.findAll(pageable).map(groupMemberMapper::toDto);
    }

    /**
     * Get one groupMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GroupMemberDTO> findOne(Long id) {
        log.debug("Request to get GroupMember : {}", id);
        return groupMemberRepository.findById(id).map(groupMemberMapper::toDto);
    }

    /**
     * Delete the groupMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GroupMember : {}", id);
        groupMemberRepository.deleteById(id);
    }
}
