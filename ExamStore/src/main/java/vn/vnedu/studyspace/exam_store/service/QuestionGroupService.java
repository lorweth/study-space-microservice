package vn.vnedu.studyspace.exam_store.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.Question;
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionRepository;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionGroupDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.QuestionGroupMapper;
import vn.vnedu.studyspace.exam_store.service.mapper.QuestionMapper;

/**
 * Service Implementation for managing {@link QuestionGroup}.
 */
@Service
@Transactional
public class QuestionGroupService {

    private final Logger log = LoggerFactory.getLogger(QuestionGroupService.class);

    private final QuestionGroupRepository questionGroupRepository;

    private final QuestionRepository questionRepository;

    private final QuestionGroupMapper questionGroupMapper;

    private final QuestionMapper questionMapper;

    public QuestionGroupService(
        QuestionGroupRepository questionGroupRepository,
        QuestionGroupMapper questionGroupMapper,
        QuestionRepository questionRepository,
        QuestionMapper questionMapper
    ) {
        this.questionGroupRepository = questionGroupRepository;
        this.questionGroupMapper = questionGroupMapper;
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
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

    public QuestionGroupDTO saveWithUserLogin(QuestionGroupDTO dto, String userLogin){
        dto.setUserLogin(userLogin);
        dto.setGroupId(null);
        return save(dto);
    }

    public QuestionGroupDTO saveWithGroupId(QuestionGroupDTO dto, Long groupId){
        dto.setUserLogin(null);
        dto.setGroupId(groupId);
        return save(dto);
    }

    /**
     * Save exam to new questionGroup of current user.
     *
     * @param dto the questionGroup DTO.
     * @param userLogin the username.
     * @return the questionGroup with list of question.
     */
    public QuestionGroupDTO saveWithQuestions(QuestionGroupDTO dto, String userLogin) {
        // store question list
        Set<QuestionDTO> questions = dto.getQuestions();

        dto.setGroupId(null);
        dto.setUserLogin(userLogin);
        // save new question group
        dto = save(dto);

        // foreach question in list save new it with group "dto.getId()" and store it in "newQuestions"
        Set<QuestionDTO> newQuestions = new HashSet<>();
        for(QuestionDTO questionDTO: questions){
            questionDTO.setQuestionGroup(dto);
            Question question = questionMapper.toEntity(questionDTO);
            question = questionRepository.save(question);
            newQuestions.add(questionMapper.toDto(question));
        }

        // add "newQuestions" to "dto"
        dto.setQuestions(newQuestions);
        return dto;
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
