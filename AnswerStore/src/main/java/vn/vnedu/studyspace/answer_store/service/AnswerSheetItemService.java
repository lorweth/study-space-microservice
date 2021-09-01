package vn.vnedu.studyspace.answer_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetItemRepository;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetItemDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.AnswerSheetItemMapper;

/**
 * Service Implementation for managing {@link AnswerSheetItem}.
 */
@Service
@Transactional
public class AnswerSheetItemService {

    private final Logger log = LoggerFactory.getLogger(AnswerSheetItemService.class);

    private final AnswerSheetItemRepository answerSheetItemRepository;

    private final AnswerSheetItemMapper answerSheetItemMapper;

    public AnswerSheetItemService(AnswerSheetItemRepository answerSheetItemRepository, AnswerSheetItemMapper answerSheetItemMapper) {
        this.answerSheetItemRepository = answerSheetItemRepository;
        this.answerSheetItemMapper = answerSheetItemMapper;
    }

    /**
     * Save a answerSheetItem.
     *
     * @param answerSheetItemDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AnswerSheetItemDTO> save(AnswerSheetItemDTO answerSheetItemDTO) {
        log.debug("Request to save AnswerSheetItem : {}", answerSheetItemDTO);
        return answerSheetItemRepository.save(answerSheetItemMapper.toEntity(answerSheetItemDTO)).map(answerSheetItemMapper::toDto);
    }

    /**
     * Partially update a answerSheetItem.
     *
     * @param answerSheetItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AnswerSheetItemDTO> partialUpdate(AnswerSheetItemDTO answerSheetItemDTO) {
        log.debug("Request to partially update AnswerSheetItem : {}", answerSheetItemDTO);

        return answerSheetItemRepository
            .findById(answerSheetItemDTO.getId())
            .map(
                existingAnswerSheetItem -> {
                    answerSheetItemMapper.partialUpdate(existingAnswerSheetItem, answerSheetItemDTO);

                    return existingAnswerSheetItem;
                }
            )
            .flatMap(answerSheetItemRepository::save)
            .map(answerSheetItemMapper::toDto);
    }

    /**
     * Get all the answerSheetItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AnswerSheetItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnswerSheetItems");
        return answerSheetItemRepository.findAllBy(pageable).map(answerSheetItemMapper::toDto);
    }

    /**
     * Returns the number of answerSheetItems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return answerSheetItemRepository.count();
    }

    /**
     * Get one answerSheetItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AnswerSheetItemDTO> findOne(Long id) {
        log.debug("Request to get AnswerSheetItem : {}", id);
        return answerSheetItemRepository.findById(id).map(answerSheetItemMapper::toDto);
    }

    /**
     * Delete the answerSheetItem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AnswerSheetItem : {}", id);
        return answerSheetItemRepository.deleteById(id);
    }
}
