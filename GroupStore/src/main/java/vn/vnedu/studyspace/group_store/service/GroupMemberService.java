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
import vn.vnedu.studyspace.group_store.service.factory.GroupFactory;
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
     * Save a new groupMember.
     *
     * @param groupId id of the group.
     * @param userLogin current username logged in.
     * @return the persisted entity.
     */
    private GroupMemberDTO saveAs(Long groupId, String userLogin, Integer role) {
        log.debug("Request to save A member name {} with role {} in group {}", userLogin, role, groupId);
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(GroupFactory.getGroup(groupId));
        groupMember.setUserLogin(userLogin);
        groupMember.setRole(role);
        groupMember = groupMemberRepository.save(groupMember);
        return groupMemberMapper.toDto(groupMember);
    }

    public GroupMemberDTO saveAsWaiting(Long groupId, String userLogin) {
        return saveAs(groupId, userLogin, 0);
    }

    public GroupMemberDTO saveAsMember(Long groupId, String userLogin) {
        return saveAs(groupId, userLogin, 1);
    }

    public GroupMemberDTO saveAsAdmin(Long groupId, String userLogin) {
        return saveAs(groupId, userLogin, 2);
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
     * Get all the groupMembers in the group
     *
     * @param groupId the id of the group.
     * @param pageable the pagination information.
     * @return the list of groupMembers.
     */
    @Transactional(readOnly = true)
    public Page<GroupMemberDTO> findAllByGroupId(Long groupId, Pageable pageable) {
        log.debug("Request to get all GroupMembers in Group {}", groupId);
        return groupMemberRepository.findAllByGroupId(groupId, pageable).map(groupMemberMapper::toDto);
    }

    /**
     * Get member in groupMember.
     *
     * @param userLogin member name.
     * @param groupId id of the group.
     * @return the persisted entity.
     */
    @Transactional(readOnly = true)
    public Optional<GroupMemberDTO> findByUserLoginAndGroupId(String userLogin, Long groupId){
        log.debug("Request to get GroupMember {} in Group {}", userLogin, groupId);
        return groupMemberRepository.findByUserLoginAndGroupId(userLogin, groupId).map(groupMemberMapper::toDto);
    }

    /**
     * Find all GroupMember in "groupId" with permission "role".
     *
     * @param groupId the "id" group.
     * @param role the permission.
     * @param pageable the pagination infomation.
     * @return the list of entity.
     */
    private Page<GroupMemberDTO> findAllByGroupIdAndRole(Long groupId, Integer role, Pageable pageable) {
        log.debug("Request to get GroupMembers in Group {} with Role {}", groupId, role);
        return groupMemberRepository.findAllByGroupIdAndRole(groupId, role, pageable).map(groupMemberMapper::toDto);
    }

    public Page<GroupMemberDTO> findAllAdminInGroup(Long groupId, Pageable pageable) {
        return findAllByGroupIdAndRole(groupId, 2, pageable);
    }

    public Page<GroupMemberDTO> findAllMemberInGroup(Long groupId, Pageable pageable) {
        return findAllByGroupIdAndRole(groupId, 1, pageable);
    }

    public Page<GroupMemberDTO> findAllWaitingInGroup(Long groupId, Pageable pageable) {
        return findAllByGroupIdAndRole(groupId, 0, pageable);
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
