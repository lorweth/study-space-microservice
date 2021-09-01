package vn.vnedu.studyspace.answer_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;
import vn.vnedu.studyspace.answer_store.repository.GroupTimeTableRepository;
import vn.vnedu.studyspace.answer_store.service.dto.GroupTimeTableDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.GroupTimeTableMapper;

/**
 * Service Implementation for managing {@link GroupTimeTable}.
 */
@Service
@Transactional
public class GroupTimeTableService {

    private final Logger log = LoggerFactory.getLogger(GroupTimeTableService.class);

    private final GroupTimeTableRepository groupTimeTableRepository;

    private final GroupTimeTableMapper groupTimeTableMapper;

    public GroupTimeTableService(GroupTimeTableRepository groupTimeTableRepository, GroupTimeTableMapper groupTimeTableMapper) {
        this.groupTimeTableRepository = groupTimeTableRepository;
        this.groupTimeTableMapper = groupTimeTableMapper;
    }

    /**
     * Save a groupTimeTable.
     *
     * @param groupTimeTableDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<GroupTimeTableDTO> save(GroupTimeTableDTO groupTimeTableDTO) {
        log.debug("Request to save GroupTimeTable : {}", groupTimeTableDTO);
        return groupTimeTableRepository.save(groupTimeTableMapper.toEntity(groupTimeTableDTO)).map(groupTimeTableMapper::toDto);
    }

    /**
     * Partially update a groupTimeTable.
     *
     * @param groupTimeTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<GroupTimeTableDTO> partialUpdate(GroupTimeTableDTO groupTimeTableDTO) {
        log.debug("Request to partially update GroupTimeTable : {}", groupTimeTableDTO);

        return groupTimeTableRepository
            .findById(groupTimeTableDTO.getId())
            .map(
                existingGroupTimeTable -> {
                    groupTimeTableMapper.partialUpdate(existingGroupTimeTable, groupTimeTableDTO);

                    return existingGroupTimeTable;
                }
            )
            .flatMap(groupTimeTableRepository::save)
            .map(groupTimeTableMapper::toDto);
    }

    /**
     * Get all the groupTimeTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<GroupTimeTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GroupTimeTables");
        return groupTimeTableRepository.findAllBy(pageable).map(groupTimeTableMapper::toDto);
    }

    /**
     * Returns the number of groupTimeTables available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return groupTimeTableRepository.count();
    }

    /**
     * Get one groupTimeTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<GroupTimeTableDTO> findOne(Long id) {
        log.debug("Request to get GroupTimeTable : {}", id);
        return groupTimeTableRepository.findById(id).map(groupTimeTableMapper::toDto);
    }

    /**
     * Delete the groupTimeTable by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete GroupTimeTable : {}", id);
        return groupTimeTableRepository.deleteById(id);
    }
}
