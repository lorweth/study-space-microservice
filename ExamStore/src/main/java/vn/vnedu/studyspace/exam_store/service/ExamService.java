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
import vn.vnedu.studyspace.exam_store.domain.Exam;
import vn.vnedu.studyspace.exam_store.domain.ExamItem;
import vn.vnedu.studyspace.exam_store.repository.ExamRepository;
import vn.vnedu.studyspace.exam_store.repository.OptionRepository;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;
import vn.vnedu.studyspace.exam_store.service.dto.OptionDTO;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.ExamMapper;

/**
 * Service Implementation for managing {@link Exam}.
 */
@Service
@Transactional
public class ExamService {

    private final Logger log = LoggerFactory.getLogger(ExamService.class);

    private final ExamRepository examRepository;

    private final QuestionGroupService questionGroupService;

    private final OptionService optionService;

    private final QuestionService questionService;

    private final ExamItemService examItemService;

    private final ExamMapper examMapper;

    private Random generator = new Random();

    public ExamService(ExamRepository examRepository, QuestionGroupService questionGroupService, QuestionService questionService, OptionService optionService, ExamItemService examItemService, ExamMapper examMapper) {
        this.examRepository = examRepository;
        this.questionGroupService = questionGroupService;
        this.questionService = questionService;
        this.optionService = optionService;
        this.examItemService = examItemService;
        this.examMapper = examMapper;
    }

    /**
     * Save a exam.
     *
     * @param examDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamDTO save(ExamDTO examDTO) {
        log.debug("Request to save Exam : {}", examDTO);
        Exam exam = examMapper.toEntity(examDTO);
        exam = examRepository.save(exam);
        return examMapper.toDto(exam);
    }

    /**
     * Partially update a exam.
     *
     * @param examDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamDTO> partialUpdate(ExamDTO examDTO) {
        log.debug("Request to partially update Exam : {}", examDTO);

        return examRepository
            .findById(examDTO.getId())
            .map(
                existingExam -> {
                    examMapper.partialUpdate(existingExam, examDTO);

                    return existingExam;
                }
            )
            .map(examRepository::save)
            .map(examMapper::toDto);
    }

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Exams");
        return examRepository.findAll(pageable).map(examMapper::toDto);
    }

    /**
     * Get all the Exams by Group.
     *
     * @param groupId the id of the Group.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamDTO> findByGroup(long groupId, Pageable pageable) {
        log.debug("Request to get all Exams by Group: {}", groupId);
        return examRepository.findByGroupId(groupId, pageable).map(examMapper::toDto);
    }

    /**
     * Get one exam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExamDTO> findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        return examRepository.findById(id).map(examMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestions(long examId) {
        log.debug("Request to get questions of the Exam: {}", examId);
        // Get all Item of the Exam
        List<ExamItemDTO> examItemList = examItemService.findByExam(examId);

        // Store result
        List<QuestionDTO> result = new LinkedList<>();

        for (ExamItemDTO item : examItemList) {
            // Get all question in QuestionGroup
            List<QuestionDTO> questionList = questionService.findByRepo(item.getRepo().getId());

            // Remove some question random
            while (item.getNumOfQuestion() < questionList.size()){
                int removeIndex = generator.nextInt(questionList.size() - 1);
                questionList.remove(removeIndex);
            }

            // get and set Option for each Question
            for(QuestionDTO q: questionList) {
                List<OptionDTO> optionList = optionService.findByQuestion(q.getId());
                q.setOptions(new HashSet<>(optionList));
            }

            // Concat new questionList into result list
            result = Stream.concat(result.stream(), questionList.stream()).collect(Collectors.toList());
        }

        return result;
    }

    /**
     * Delete the exam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }
}
