package vn.vnedu.studyspace.answer_store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupMember;
import vn.vnedu.studyspace.answer_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.answer_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.GroupMemberMapper;

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
    public Mono<GroupMemberDTO> save(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to save GroupMember : {}", groupMemberDTO);
        return groupMemberRepository.save(groupMemberMapper.toEntity(groupMemberDTO)).map(groupMemberMapper::toDto);
    }

    /**
     * Insert a groupMember.
     *
     * @param groupMemberDTO the entity to insert.
     * @return the persisted entity.
     */
    public Mono<GroupMemberDTO> insert(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to insert GroupMember : {}", groupMemberDTO);
        return groupMemberRepository.insert(groupMemberMapper.toEntity(groupMemberDTO)).map(groupMemberMapper::toDto);
    }

    /**
     * Partially update a groupMember.
     *
     * @param groupMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GroupMemberDTO> partialUpdate(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to partially update GroupMember : {}", groupMemberDTO);

        return groupMemberRepository
            .findById(groupMemberDTO.getId())
            .map(
                existingGroupMember -> {
                    groupMemberMapper.partialUpdate(existingGroupMember, groupMemberDTO);

                    return existingGroupMember;
                }
            )
            .flatMap(groupMemberRepository::save)
            .map(groupMemberMapper::toDto);
    }

    /**
     * Get all the groupMembers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<GroupMemberDTO> findAll() {
        log.debug("Request to get all GroupMembers");
        return groupMemberRepository.findAll().map(groupMemberMapper::toDto);
    }

    /**
     * Returns the number of groupMembers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return groupMemberRepository.count();
    }

    /**
     * Get one groupMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<GroupMemberDTO> findOne(Long id) {
        log.debug("Request to get GroupMember : {}", id);
        return groupMemberRepository.findById(id).map(groupMemberMapper::toDto);
    }

    /**
     * Delete the groupMember by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete GroupMember : {}", id);
        return groupMemberRepository.deleteById(id);
    }
}
