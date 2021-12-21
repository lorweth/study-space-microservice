package vn.vnedu.studyspace.exam_store.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.Exam;
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.repository.ExamRepository;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.ExamMapper;

/**
 * Service Implementation for managing {@link Exam}.
 */
@Service
@Transactional
public class ExamService {

    private final Logger log = LoggerFactory.getLogger(ExamService.class);

    private final ExamRepository examRepository;

    private final ExamMapper examMapper;

    private final ExamItemRepository examItemRepository;

    public ExamService(ExamRepository examRepository, ExamItemRepository examItemRepository, ExamMapper examMapper) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.examItemRepository = examItemRepository;
    }

    /**
     * Save an exam.
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
     * Partially update an exam.
     *
     * @param examDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamDTO> partialUpdate(ExamDTO examDTO) {
        log.debug("Request to partially update Exam : {}", examDTO);

        return examRepository
            .findById(examDTO.getId())
            .map(existingExam -> {
                examMapper.partialUpdate(existingExam, examDTO);

                return existingExam;
            })
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
     * Get all the exams in Group "groupId".
     *
     * @param groupId the id of the group.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamDTO> findAllByGroupId(Long groupId, Pageable pageable){
        log.debug("Request to get all Exams in Group {}", groupId);
        return examRepository.findAllByGroupIdOrderByStartAtDesc(groupId, pageable).map(examMapper::toDto);
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

    /**
     * Delete the exam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }

    /**
     * Delete the exam by id.
     * @param id the id of the exam.
     */
    public void deleteWithItems(Long id) {
        log.debug("Request to delete Exam {} with all Items", id);
        examItemRepository.deleteByExamId(id);
        examRepository.deleteById(id);
    }
}
