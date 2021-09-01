package vn.vnedu.studyspace.answer_store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetItemRepository;
import vn.vnedu.studyspace.answer_store.service.AnswerSheetItemService;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetItemDTO;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem}.
 */
@RestController
@RequestMapping("/api")
public class AnswerSheetItemResource {

    private final Logger log = LoggerFactory.getLogger(AnswerSheetItemResource.class);

    private static final String ENTITY_NAME = "answerStoreAnswerSheetItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerSheetItemService answerSheetItemService;

    private final AnswerSheetItemRepository answerSheetItemRepository;

    public AnswerSheetItemResource(AnswerSheetItemService answerSheetItemService, AnswerSheetItemRepository answerSheetItemRepository) {
        this.answerSheetItemService = answerSheetItemService;
        this.answerSheetItemRepository = answerSheetItemRepository;
    }

    /**
     * {@code POST  /answer-sheet-items} : Create a new answerSheetItem.
     *
     * @param answerSheetItemDTO the answerSheetItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerSheetItemDTO, or with status {@code 400 (Bad Request)} if the answerSheetItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answer-sheet-items")
    public Mono<ResponseEntity<AnswerSheetItemDTO>> createAnswerSheetItem(@Valid @RequestBody AnswerSheetItemDTO answerSheetItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save AnswerSheetItem : {}", answerSheetItemDTO);
        if (answerSheetItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new answerSheetItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return answerSheetItemService
            .save(answerSheetItemDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/answer-sheet-items/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /answer-sheet-items/:id} : Updates an existing answerSheetItem.
     *
     * @param id the id of the answerSheetItemDTO to save.
     * @param answerSheetItemDTO the answerSheetItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerSheetItemDTO,
     * or with status {@code 400 (Bad Request)} if the answerSheetItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerSheetItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/answer-sheet-items/{id}")
    public Mono<ResponseEntity<AnswerSheetItemDTO>> updateAnswerSheetItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnswerSheetItemDTO answerSheetItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AnswerSheetItem : {}, {}", id, answerSheetItemDTO);
        if (answerSheetItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerSheetItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return answerSheetItemRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return answerSheetItemService
                        .save(answerSheetItemDTO)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /answer-sheet-items/:id} : Partial updates given fields of an existing answerSheetItem, field will ignore if it is null
     *
     * @param id the id of the answerSheetItemDTO to save.
     * @param answerSheetItemDTO the answerSheetItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerSheetItemDTO,
     * or with status {@code 400 (Bad Request)} if the answerSheetItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the answerSheetItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the answerSheetItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/answer-sheet-items/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<AnswerSheetItemDTO>> partialUpdateAnswerSheetItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnswerSheetItemDTO answerSheetItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnswerSheetItem partially : {}, {}", id, answerSheetItemDTO);
        if (answerSheetItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerSheetItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return answerSheetItemRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<AnswerSheetItemDTO> result = answerSheetItemService.partialUpdate(answerSheetItemDTO);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /answer-sheet-items} : get all the answerSheetItems.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answerSheetItems in body.
     */
    @GetMapping("/answer-sheet-items")
    public Mono<ResponseEntity<List<AnswerSheetItemDTO>>> getAllAnswerSheetItems(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of AnswerSheetItems");
        return answerSheetItemService
            .countAll()
            .zipWith(answerSheetItemService.findAll(pageable).collectList())
            .map(
                countWithEntities -> {
                    return ResponseEntity
                        .ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                UriComponentsBuilder.fromHttpRequest(request),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2());
                }
            );
    }

    /**
     * {@code GET  /answer-sheet-items/:id} : get the "id" answerSheetItem.
     *
     * @param id the id of the answerSheetItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerSheetItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/answer-sheet-items/{id}")
    public Mono<ResponseEntity<AnswerSheetItemDTO>> getAnswerSheetItem(@PathVariable Long id) {
        log.debug("REST request to get AnswerSheetItem : {}", id);
        Mono<AnswerSheetItemDTO> answerSheetItemDTO = answerSheetItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerSheetItemDTO);
    }

    /**
     * {@code DELETE  /answer-sheet-items/:id} : delete the "id" answerSheetItem.
     *
     * @param id the id of the answerSheetItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/answer-sheet-items/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAnswerSheetItem(@PathVariable Long id) {
        log.debug("REST request to delete AnswerSheetItem : {}", id);
        return answerSheetItemService
            .delete(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}
