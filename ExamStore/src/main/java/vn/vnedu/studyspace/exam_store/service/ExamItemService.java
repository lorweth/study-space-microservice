package vn.vnedu.studyspace.exam_store.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.ExamItem;
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.ExamItemMapper;

/**
 * Service Implementation for managing {@link ExamItem}.
 */
@Service
@Transactional
public class ExamItemService {

    private final Logger log = LoggerFactory.getLogger(ExamItemService.class);

    private final ExamItemRepository examItemRepository;

    private final ExamItemMapper examItemMapper;

    public ExamItemService(ExamItemRepository examItemRepository, ExamItemMapper examItemMapper) {
        this.examItemRepository = examItemRepository;
        this.examItemMapper = examItemMapper;
    }

    /**
     * Save a examItem.
     *
     * @param examItemDTO the entity to save.
     * @return the persisted entity.
     */
    public ExamItemDTO save(ExamItemDTO examItemDTO) {
        log.debug("Request to save ExamItem : {}", examItemDTO);
        ExamItem examItem = examItemMapper.toEntity(examItemDTO);
        examItem = examItemRepository.save(examItem);
        return examItemMapper.toDto(examItem);
    }

    /**
     * Partially update a examItem.
     *
     * @param examItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamItemDTO> partialUpdate(ExamItemDTO examItemDTO) {
        log.debug("Request to partially update ExamItem : {}", examItemDTO);

        return examItemRepository
            .findById(examItemDTO.getId())
            .map(existingExamItem -> {
                examItemMapper.partialUpdate(existingExamItem, examItemDTO);

                return existingExamItem;
            })
            .map(examItemRepository::save)
            .map(examItemMapper::toDto);
    }

    /**
     * Get all the examItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExamItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamItems");
        return examItemRepository.findAll(pageable).map(examItemMapper::toDto);
    }

    /**
     * Get all the examItems in exam "examId".
     *
     * @param examId the id of the exam.
     * @return the list of entity.
     */
    @Transactional(readOnly = true)
    public List<ExamItemDTO> findAllByExamId(Long examId) {
        log.debug("Request to get all ExamItem in Exam {}", examId);
        return examItemRepository.findAllByExamId(examId).stream().map(examItemMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one examItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExamItemDTO> findOne(Long id) {
        log.debug("Request to get ExamItem : {}", id);
        return examItemRepository.findById(id).map(examItemMapper::toDto);
    }

    /**
     * Delete the examItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExamItem : {}", id);
        examItemRepository.deleteById(id);
    }
}
