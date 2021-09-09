package vn.vnedu.studyspace.group_store.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.group_store.domain.GroupMember;
import vn.vnedu.studyspace.group_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.group_store.service.dto.GroupDTO;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.service.mapper.GroupMemberMapper;
import vn.vnedu.studyspace.group_store.web.rest.errors.BadRequestAlertException;

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
     * Save a groupMember as admin for group.
     *
     * @param groupDTO this dto contain group
     * @param userLogin string contain user login
     * @return the persisted entity.
     */
    public GroupMemberDTO saveAdmin(GroupDTO groupDTO, String userLogin) {
        return saveUserAs(groupDTO, userLogin, 2);
    }

    /**
     * Save a groupMember as user for group.
     *
     * @param groupDTO this dto of group.
     * @param userLogin name of user.
     * @return the persisted entity.
     */
    public GroupMemberDTO saveMember(GroupDTO groupDTO, String userLogin) {
        return saveUserAs(groupDTO, userLogin, 1);
    }

    /**
     * Save a groupMember as waiting member for group.
     *
     * @param groupDTO this dto of group.
     * @param userLogin name of user.
     * @return the persisted entity.
     */
    public GroupMemberDTO saveWaitingMember(GroupDTO groupDTO, String userLogin) {
        return saveUserAs(groupDTO, userLogin, 0);
    }

    public GroupMemberDTO saveUserAs(GroupDTO groupDTO, String userLogin, Integer role) {
       GroupMemberDTO groupMemberDTO = new GroupMemberDTO();
       groupMemberDTO.setGroup(groupDTO);
       groupMemberDTO.setUserLogin(userLogin);
       groupMemberDTO.setRole(role);
       return save(groupMemberDTO);
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
     * Check if the user is admin of the group.
     *
     * @param userLogin the user login.
     * @param groupId id of the group.
     * @return the boolean.
     */
    public Boolean isAdmin(String userLogin, Long groupId) {
        return checkRole(userLogin, groupId) == 2;
    }

    /**
     * Get role of user for the group.
     *
     * @param userLogin user login.
     * @param groupId id of the group.
     * @return the number of role, -1 is not exists entity.
     */
    public Integer checkRole(String userLogin, Long groupId) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByUserLoginAndGroup_Id(userLogin, groupId);
        if(groupMember.isEmpty()){
            return -1; // Khong co quyen
        }
        return groupMember.get().getRole();
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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GroupMemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GroupMembers");
        return groupMemberRepository.findAll(pageable).map(groupMemberMapper::toDto);
    }

    /**
     * find all entity by groupId.
     *
     * @param groupId the id of group relation with entity.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GroupMemberDTO> findAllByGroupId(Long groupId) {
        log.debug("Request to get all GroupMembers by GroupID: {}", groupId);
        return groupMemberRepository
            .findByGroup_Id(groupId)
            .stream()
            .map(groupMemberMapper::toDto)
            .collect(Collectors.toList());
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
