package vn.vnedu.studyspace.exam_store.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.ExamItem;
import vn.vnedu.studyspace.exam_store.domain.Option;
import vn.vnedu.studyspace.exam_store.domain.Question;
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.repository.OptionRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionRepository;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;
import vn.vnedu.studyspace.exam_store.service.dto.OptionDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.OptionMapper;
import vn.vnedu.studyspace.exam_store.service.mapper.QuestionMapper;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;

    private final QuestionMapper questionMapper;

    private final OptionMapper optionMapper;

    private final ExamItemRepository examItemRepository;

    // Random object.
    private final Random generator = new Random();

    public QuestionService(
        QuestionRepository questionRepository,
        QuestionMapper questionMapper,
        OptionRepository optionRepository,
        OptionMapper optionMapper,
        ExamItemRepository examItemRepository
    ) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
        this.examItemRepository = examItemRepository;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Set<OptionDTO> optionDTOSet = questionDTO.getOptions();

        // save question
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);

        // foreach option in "optionDTOSet" save it with question id into "newOptions"
        Set<OptionDTO> newOptions = new HashSet<>();
        for(OptionDTO optionDTO: optionDTOSet){
            Option option = optionMapper.toEntity(optionDTO);
            option.setQuestion(question);
            option = optionRepository.save(option);
            newOptions.add(optionMapper.toDto(option));
        }

        QuestionDTO dto = questionMapper.toDto(question);
        dto.setOptions(newOptions);
        return dto;
    }

    /**
     * Partially update a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        log.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .map(questionRepository::save)
            .map(questionMapper::toDto);
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable).map(questionMapper::toDto);
    }

    /**
     * Get all questions in questionGroup "repoId".
     *
     * @param repoId the id of the questionGroup.
     * @param pageable the pagination information.
     * @return the list of entity.
     */
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAllByRepoId(Long repoId, Pageable pageable) {
        log.debug("Request to get all Question in QuestionGroup {}", repoId);
        return questionRepository
            .findAllByQuestionGroupId(repoId, pageable)
            .map(questionMapper::toDto);
    }

    /**
     * Get some random question in exam "examId".
     *
     * @param examId the id of the exam.
     * @return the list of entity.
     */
    @Transactional(readOnly = true)
    public List<QuestionDTO> findAllByExamId(Long examId) {
        log.debug("Request to get all Question in Exam {}", examId);

        // Get all items in exam.
        List<ExamItem> items = examItemRepository.findAllByExamId(examId);
        // A collection to store question.
        List<Question> result = new LinkedList<>();

        for (ExamItem item : items) {
            // Get all question in QuestionGroup
            List<Question> questionList = questionRepository.findByQuestionGroupId(item.getQuestionGroup().getId());

            // Remove some question random
            while (item.getNumOfQuestion() < questionList.size()){
                int removeIndex = generator.nextInt(questionList.size() - 1);
                questionList.remove(removeIndex);
            }

            // Concat new questionList into result list
            result = Stream.concat(result.stream(), questionList.stream()).collect(Collectors.toList());
        }

        return result.stream().map(questionMapper::toDtoIgnoreCorrectOption).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> findQuestions(List<ExamItemDTO> items) {
        log.debug("Request to get all Question for Tu hoc ");

        // A collection to store question.
        List<Question> result = new LinkedList<>();
        for (ExamItemDTO item : items) {
            // Get all question in QuestionGroup
            List<Question> questionList = questionRepository.findByQuestionGroupId(item.getQuestionGroup().getId());

            // Remove some question random
            while (item.getNumOfQuestion() < questionList.size()){
                int removeIndex = generator.nextInt(questionList.size() - 1);
                questionList.remove(removeIndex);
            }

            // Concat new questionList into result list
            result = Stream.concat(result.stream(), questionList.stream()).collect(Collectors.toList());
        }

        return result.stream().map(questionMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id).map(questionMapper::toDto);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }

    public void deleteWithOption(Long id){
        log.debug("Request to delete Question : {} and all option in it", id);
        optionRepository.deleteAllByQuestionId(id);
        questionRepository.deleteById(id);
    }
}
