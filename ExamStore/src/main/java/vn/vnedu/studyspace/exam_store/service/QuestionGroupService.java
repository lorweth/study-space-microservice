package vn.vnedu.studyspace.exam_store.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionGroupDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.QuestionGroupMapper;

/**
 * Service Implementation for managing {@link QuestionGroup}.
 */
@Service
@Transactional
public class QuestionGroupService {

    private final Logger log = LoggerFactory.getLogger(QuestionGroupService.class);

    private final QuestionGroupRepository questionGroupRepository;

    private final QuestionGroupMapper questionGroupMapper;

    public QuestionGroupService(QuestionGroupRepository questionGroupRepository, QuestionGroupMapper questionGroupMapper) {
        this.questionGroupRepository = questionGroupRepository;
        this.questionGroupMapper = questionGroupMapper;
    }

    /**
     * Save a questionGroup.
     *
     * @param questionGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionGroupDTO save(QuestionGroupDTO questionGroupDTO) {
        log.debug("Request to save QuestionGroup : {}", questionGroupDTO);
        QuestionGroup questionGroup = questionGroupMapper.toEntity(questionGroupDTO);
        questionGroup = questionGroupRepository.save(questionGroup);
        return questionGroupMapper.toDto(questionGroup);
    }

    /**
     * Partially update a questionGroup.
     *
     * @param questionGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionGroupDTO> partialUpdate(QuestionGroupDTO questionGroupDTO) {
        log.debug("Request to partially update QuestionGroup : {}", questionGroupDTO);

        return questionGroupRepository
            .findById(questionGroupDTO.getId())
            .map(existingQuestionGroup -> {
                questionGroupMapper.partialUpdate(existingQuestionGroup, questionGroupDTO);

                return existingQuestionGroup;
            })
            .map(questionGroupRepository::save)
            .map(questionGroupMapper::toDto);
    }

    /**
     * Get all the questionGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuestionGroups");
        return questionGroupRepository.findAll(pageable).map(questionGroupMapper::toDto);
    }

    /**
     * Get one questionGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionGroupDTO> findOne(Long id) {
        log.debug("Request to get QuestionGroup : {}", id);
        return questionGroupRepository.findById(id).map(questionGroupMapper::toDto);
    }

    /**
     * Delete the questionGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionGroup : {}", id);
        questionGroupRepository.deleteById(id);
    }
}
