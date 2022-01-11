package vn.vnedu.studyspace.answer_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetItemRepository;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetRepository;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetDTO;
import vn.vnedu.studyspace.answer_store.service.dto.CorrectAnswerDTO;
import vn.vnedu.studyspace.answer_store.service.dto.UserAnswerDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.AnswerSheetMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Service Implementation for managing {@link AnswerSheet}.
 */
@Service
@Transactional
public class AnswerSheetService {

    private final Logger log = LoggerFactory.getLogger(AnswerSheetService.class);

    @Autowired
    private FeignClientService feignClientService;

    private final AnswerSheetRepository answerSheetRepository;

    private final AnswerSheetItemRepository answerSheetItemRepository;

    private final AnswerSheetMapper answerSheetMapper;

    public AnswerSheetService(AnswerSheetRepository answerSheetRepository, AnswerSheetMapper answerSheetMapper, AnswerSheetItemRepository answerSheetItemRepository) {
        this.answerSheetRepository = answerSheetRepository;
        this.answerSheetMapper = answerSheetMapper;
        this.answerSheetItemRepository = answerSheetItemRepository;
    }

    /**
     * Save a answerSheet.
     *
     * @param answerSheetDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AnswerSheetDTO> save(AnswerSheetDTO answerSheetDTO) {
        log.debug("Request to save AnswerSheet : {}", answerSheetDTO);
        return answerSheetRepository.save(answerSheetMapper.toEntity(answerSheetDTO)).map(answerSheetMapper::toDto);
    }

    /**
     * Partially update a answerSheet.
     *
     * @param answerSheetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AnswerSheetDTO> partialUpdate(AnswerSheetDTO answerSheetDTO) {
        log.debug("Request to partially update AnswerSheet : {}", answerSheetDTO);

        return answerSheetRepository
            .findById(answerSheetDTO.getId())
            .map(existingAnswerSheet -> {
                answerSheetMapper.partialUpdate(existingAnswerSheet, answerSheetDTO);

                return existingAnswerSheet;
            })
            .flatMap(answerSheetRepository::save)
            .map(answerSheetMapper::toDto);
    }

    /**
     * Get all the answerSheets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AnswerSheetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnswerSheets");
        return answerSheetRepository.findAllBy(pageable).map(answerSheetMapper::toDto);
    }

    /**
     * Find wrong answer of the sheet.
     *
     * @param sheetId the id of the sheet.
     * @param authorization the authorization to call request from other server.
     * @return the list of entity.
     */
    @Transactional(readOnly = true)
    public Flux<AnswerSheetItem> findWrongAnswer(Long sheetId, String authorization) {
        log.debug("Request to get all wrong answer of the answer sheet {}", sheetId);
        return answerSheetItemRepository
            .findByAnswerSheet(sheetId)
            .map(AnswerSheetItem::getQuestionId)
            .collectList()
            .flatMap(questionIdList -> feignClientService.getCorrectAnswer(authorization, questionIdList))
            .flatMap(correctAnswerList ->
                answerSheetItemRepository
                    .findByAnswerSheet(sheetId)
                    .collectList()
                    .flatMap(userOptionList -> {
                        List<AnswerSheetItem> wrongOption = new LinkedList<>();
                        for(AnswerSheetItem userOption: userOptionList){
                            for(CorrectAnswerDTO correct: correctAnswerList){
                                if(
                                    Objects.equals(userOption.getQuestionId(), correct.getQuestionId()) // same question
                                    && !Objects.equals(userOption.getAnswerId(), correct.getAnswerId()) // different answer
                                    && !wrongOption.contains(userOption) // wrongOptionList not contained it
                                ){
                                    wrongOption.add(userOption);
                                }
                            }
                        }
                        return Mono.just(wrongOption);
                    })

            )
            .flatMapMany(Flux::fromIterable);
    }

    /**
     * Returns the number of answerSheets available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return answerSheetRepository.count();
    }

    /**
     * Get one answerSheet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AnswerSheetDTO> findOne(Long id) {
        log.debug("Request to get AnswerSheet : {}", id);
        return answerSheetRepository.findById(id).map(answerSheetMapper::toDto);
    }

    /**
     * Delete the answerSheet by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AnswerSheet : {}", id);
        return answerSheetRepository.deleteById(id);
    }
}
