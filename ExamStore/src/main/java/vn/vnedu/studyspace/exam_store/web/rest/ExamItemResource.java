package vn.vnedu.studyspace.exam_store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.service.ExamItemService;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.exam_store.domain.ExamItem}.
 */
@RestController
@RequestMapping("/api")
public class ExamItemResource {

    private final Logger log = LoggerFactory.getLogger(ExamItemResource.class);

    private static final String ENTITY_NAME = "examStoreExamItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamItemService examItemService;

    private final ExamItemRepository examItemRepository;

    public ExamItemResource(ExamItemService examItemService, ExamItemRepository examItemRepository) {
        this.examItemService = examItemService;
        this.examItemRepository = examItemRepository;
    }

    /**
     * {@code POST  /exam-items} : Create a new examItem.
     *
     * @param examItemDTO the examItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examItemDTO, or with status {@code 400 (Bad Request)} if the examItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@examSecurity.hasPermission(#examItemDTO.getExam().getId(), 'ADMIN')")
    @PostMapping("/exam-items")
    public ResponseEntity<ExamItemDTO> createExamItem(@Valid @RequestBody ExamItemDTO examItemDTO) throws URISyntaxException {
        log.debug("REST request to save ExamItem : {}", examItemDTO);
        if (examItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new examItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamItemDTO result = examItemService.save(examItemDTO);
        return ResponseEntity
            .created(new URI("/api/exam-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-items/:id} : Updates an existing examItem.
     *
     * @param id the id of the examItemDTO to save.
     * @param examItemDTO the examItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examItemDTO,
     * or with status {@code 400 (Bad Request)} if the examItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@examItemSecurity.hasPermission(#id, 'ADMIN')")
    @PutMapping("/exam-items/{id}")
    public ResponseEntity<ExamItemDTO> updateExamItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamItemDTO examItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExamItem : {}, {}", id, examItemDTO);
        if (examItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamItemDTO result = examItemService.save(examItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-items/:id} : Partial updates given fields of an existing examItem, field will ignore if it is null
     *
     * @param id the id of the examItemDTO to save.
     * @param examItemDTO the examItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examItemDTO,
     * or with status {@code 400 (Bad Request)} if the examItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamItemDTO> partialUpdateExamItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamItemDTO examItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamItem partially : {}, {}", id, examItemDTO);
        if (examItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamItemDTO> result = examItemService.partialUpdate(examItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-items} : get all the examItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examItems in body.
     */
    @GetMapping("/exam-items")
    public ResponseEntity<List<ExamItemDTO>> getAllExamItems(Pageable pageable) {
        log.debug("REST request to get a page of ExamItems");
        Page<ExamItemDTO> page = examItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-items/exam/:examId} : get all the examItems in the exam "examId".
     *
     * @param examId the id of the exam.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examItems in body.
     */
    @PreAuthorize("@examSecurity.hasPermission(#examId, 'ADMIN')")
    @GetMapping("/exam-items/exam/{examId}")
    public ResponseEntity<List<ExamItemDTO>> getAllExamItems(@PathVariable Long examId) {
        log.debug("REST request to get a page of ExamItems");
        List<ExamItemDTO> items = examItemService.findAllByExamId(examId);
        return ResponseEntity.ok().body(items);
    }

    /**
     * {@code GET  /exam-items/:id} : get the "id" examItem.
     *
     * @param id the id of the examItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examItemDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@examItemSecurity.hasPermission(#id, 'ADMIN')")
    @GetMapping("/exam-items/{id}")
    public ResponseEntity<ExamItemDTO> getExamItem(@PathVariable Long id) {
        log.debug("REST request to get ExamItem : {}", id);
        Optional<ExamItemDTO> examItemDTO = examItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examItemDTO);
    }

    /**
     * {@code DELETE  /exam-items/:id} : delete the "id" examItem.
     *
     * @param id the id of the examItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("@examItemSecurity.hasPermission(#id, 'ADMIN')")
    @DeleteMapping("/exam-items/{id}")
    public ResponseEntity<Void> deleteExamItem(@PathVariable Long id) {
        log.debug("REST request to delete ExamItem : {}", id);
        examItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
