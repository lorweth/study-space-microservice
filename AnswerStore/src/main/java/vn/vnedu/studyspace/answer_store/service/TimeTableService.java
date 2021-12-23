package vn.vnedu.studyspace.answer_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.TimeTable;
import vn.vnedu.studyspace.answer_store.repository.TimeTableRepository;
import vn.vnedu.studyspace.answer_store.service.dto.TimeTableDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.TimeTableMapper;

/**
 * Service Implementation for managing {@link TimeTable}.
 */
@Service
@Transactional
public class TimeTableService {

    private final Logger log = LoggerFactory.getLogger(TimeTableService.class);

    private final TimeTableRepository timeTableRepository;

    private final TimeTableMapper timeTableMapper;

    public TimeTableService(TimeTableRepository timeTableRepository, TimeTableMapper timeTableMapper) {
        this.timeTableRepository = timeTableRepository;
        this.timeTableMapper = timeTableMapper;
    }

    /**
     * Save a timeTable.
     *
     * @param timeTableDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TimeTableDTO> save(TimeTableDTO timeTableDTO) {
        log.debug("Request to save TimeTable : {}", timeTableDTO);
        return timeTableRepository.save(timeTableMapper.toEntity(timeTableDTO)).map(timeTableMapper::toDto);
    }

    /**
     * Partially update a timeTable.
     *
     * @param timeTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TimeTableDTO> partialUpdate(TimeTableDTO timeTableDTO) {
        log.debug("Request to partially update TimeTable : {}", timeTableDTO);

        return timeTableRepository
            .findById(timeTableDTO.getId())
            .map(existingTimeTable -> {
                timeTableMapper.partialUpdate(existingTimeTable, timeTableDTO);

                return existingTimeTable;
            })
            .flatMap(timeTableRepository::save)
            .map(timeTableMapper::toDto);
    }

    /**
     * Get all the timeTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TimeTableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeTables");
        return timeTableRepository.findAllBy(pageable).map(timeTableMapper::toDto);
    }

    /**
     * Get all the timeTables by current user login name.
     *
     * @param userLogin the name of the user.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TimeTableDTO> findAllByUserLogin(String userLogin, Pageable pageable) {
        log.debug("Request to get all TimeTables of User {}", userLogin);
        return timeTableRepository.findAllByUserLogin(userLogin, pageable).map(timeTableMapper::toDto);
    }

    /**
     * Returns the number of timeTables available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return timeTableRepository.count();
    }

    /**
     * Count all entity with userLogin "userLogin".
     *
     * @param userLogin the name of user.
     * @return the number of entities.
     */
    public Mono<Long> countByUserLogin(String userLogin) {
        return timeTableRepository.countBy(Criteria.where("userLogin").is(userLogin));
    }

    /**
     * Get one timeTable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TimeTableDTO> findOne(Long id) {
        log.debug("Request to get TimeTable : {}", id);
        return timeTableRepository.findById(id).map(timeTableMapper::toDto);
    }

    /**
     * Delete the timeTable by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TimeTable : {}", id);
        return timeTableRepository.deleteById(id);
    }
}
