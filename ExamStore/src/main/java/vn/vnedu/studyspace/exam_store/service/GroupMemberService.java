package vn.vnedu.studyspace.exam_store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.GroupMemberMapper;

import javax.swing.*;

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

    public Optional<GroupMemberDTO> findByGroupIdAndUserLogin(Long groupId, String userLogin) {
        log.debug("Request to check exists user {} in group {}", userLogin, groupId);
        return groupMemberRepository
            .findByGroupIdAndUserLogin(groupId, userLogin)
            .map(groupMemberMapper::toDto);
    }

    public Boolean isGroupAdmin(Long groupId, String userLogin) {
        log.debug("Request to check {} is admin of {}", userLogin, groupId);
        Optional<GroupMemberDTO> dtoOptional = findByGroupIdAndUserLogin(groupId, userLogin);
        if(dtoOptional.isEmpty()) {
            return false;
        }
        log.debug("Role of user: {}", dtoOptional.get().getRole());//
        return dtoOptional.get().getRole() == 2;
    }

    public Boolean isGroupMember(Long groupId, String userLogin) {
        log.debug("Request to check {} is admin of {}", userLogin, groupId);
        Optional<GroupMemberDTO> dtoOptional = findByGroupIdAndUserLogin(groupId, userLogin);
        if(dtoOptional.isEmpty()) {
            return false;
        }
        log.debug("Role of user: {}", dtoOptional.get().getRole());//
        return dtoOptional.get().getRole() == 1;
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
            .map(
                existingGroupMember -> {
                    groupMemberMapper.partialUpdate(existingGroupMember, groupMemberDTO);

                    return existingGroupMember;
                }
            )
            .map(groupMemberRepository::save)
            .map(groupMemberMapper::toDto);
    }

    /**
     * Get all the groupMembers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GroupMemberDTO> findAll() {
        log.debug("Request to get all GroupMembers");
        return groupMemberRepository.findAll().stream().map(groupMemberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
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
