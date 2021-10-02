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
import vn.vnedu.studyspace.group_store.domain.Group;
import vn.vnedu.studyspace.group_store.repository.GroupRepository;
import vn.vnedu.studyspace.group_store.service.dto.GroupDTO;
import vn.vnedu.studyspace.group_store.service.mapper.GroupMapper;

/**
 * Service Implementation for managing {@link Group}.
 */
@Service
@Transactional
public class GroupService {

    private final Logger log = LoggerFactory.getLogger(GroupService.class);

    private final GroupRepository groupRepository;

    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    /**
     * Save a group.
     *
     * @param groupDTO the entity to save.
     * @return the persisted entity.
     */
    public GroupDTO save(GroupDTO groupDTO) {
        log.debug("Request to save Group : {}", groupDTO);
        Group group = groupMapper.toEntity(groupDTO);
        group = groupRepository.save(group);
        return groupMapper.toDto(group);
    }

    /**
     * Partially update a group.
     *
     * @param groupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GroupDTO> partialUpdate(GroupDTO groupDTO) {
        log.debug("Request to partially update Group : {}", groupDTO);

        return groupRepository
            .findById(groupDTO.getId())
            .map(
                existingGroup -> {
                    groupMapper.partialUpdate(existingGroup, groupDTO);

                    return existingGroup;
                }
            )
            .map(groupRepository::save)
            .map(groupMapper::toDto);
    }

    /**
     * Get all the groups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Groups");
        return groupRepository.findAll(pageable).map(groupMapper::toDto);
    }

    /**
     * Get all groups whose names contain.
     * @param name full-name or a part of the name of the group.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GroupDTO> findByNameContainingIgnoreCase(String name) {
        log.debug("Request to get all Groups whose names contain");
        return groupRepository
            .findByNameContainingIgnoreCase(name)
            .stream()
            .map(groupMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get one group by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GroupDTO> findOne(Long id) {
        log.debug("Request to get Group : {}", id);
        return groupRepository.findById(id).map(groupMapper::toDto);
    }

    /**
     * Delete the group by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Group : {}", id);
        groupRepository.deleteById(id);
    }
}
