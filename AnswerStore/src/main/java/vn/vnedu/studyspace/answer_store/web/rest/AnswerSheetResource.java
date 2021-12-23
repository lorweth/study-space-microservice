package vn.vnedu.studyspace.answer_store.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetRepository;
import vn.vnedu.studyspace.answer_store.security.SecurityUtils;
import vn.vnedu.studyspace.answer_store.service.AnswerSheetService;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetDTO;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.answer_store.domain.AnswerSheet}.
 */
@RestController
@RequestMapping("/api")
public class AnswerSheetResource {

    private final Logger log = LoggerFactory.getLogger(AnswerSheetResource.class);

    private static final String ENTITY_NAME = "answerStoreAnswerSheet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerSheetService answerSheetService;

    private final AnswerSheetRepository answerSheetRepository;

    public AnswerSheetResource(AnswerSheetService answerSheetService, AnswerSheetRepository answerSheetRepository) {
        this.answerSheetService = answerSheetService;
        this.answerSheetRepository = answerSheetRepository;
    }

    /**
     * {@code POST  /answer-sheets} : Create a new answerSheet.
     *
     * @param answerSheetDTO the answerSheetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerSheetDTO, or with status {@code 400 (Bad Request)} if the answerSheet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/answer-sheets")
    public Mono<ResponseEntity<AnswerSheetDTO>> createAnswerSheet(@Valid @RequestBody AnswerSheetDTO answerSheetDTO) throws URISyntaxException{
        log.debug("REST request to save AnswerSheet : {}", answerSheetDTO);
        if (answerSheetDTO.getId() != null){
            throw new BadRequestAlertException("A new answerSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        answerSheetDTO.setGroupTimeTable(null); // clear groupTimeTable... this answer sheet of user self learn
        answerSheetDTO.setTime(Instant.now()); // set current time.
        return SecurityUtils
            .getCurrentUserLogin()
            .map(userLogin -> {
                answerSheetDTO.setUserLogin(userLogin);
                return answerSheetDTO;
            })
            .flatMap(answerSheetService::save)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/answer-sheets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code POST  /answer-sheets/group-time-table/:timeTableId} : Create a new answerSheet for current user login in groupTimeTable.
     *
     * @param answerSheetDTO the answerSheetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerSheetDTO, or with status {@code 400 (Bad Request)} if the answerSheet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("@groupTimeTableSecurity.hasPermission(#timeTableId, 'MEMBER')")
    @PostMapping("/answer-sheets/group-time-table/{timeTableId}")
    public Mono<ResponseEntity<AnswerSheetDTO>> createAnswerSheet(@PathVariable Long timeTableId, @Valid @RequestBody AnswerSheetDTO answerSheetDTO)
        throws URISyntaxException {
        log.debug("REST request to save AnswerSheet : {}", answerSheetDTO);
        if (answerSheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new answerSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(!Objects.equals(timeTableId, answerSheetDTO.getGroupTimeTable().getId())){
            throw new BadRequestAlertException("Invalid Id", ENTITY_NAME, "invalidId");
        }
        answerSheetDTO.setTime(Instant.now()); // set current time
        return SecurityUtils
            .getCurrentUserLogin()
            .map(userLogin -> {
                answerSheetDTO.setUserLogin(userLogin);
                return answerSheetDTO;
            })
            .flatMap(answerSheetService::save)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/answer-sheets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /answer-sheets/:id} : Updates an existing answerSheet.
     *
     * @param id the id of the answerSheetDTO to save.
     * @param answerSheetDTO the answerSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerSheetDTO,
     * or with status {@code 400 (Bad Request)} if the answerSheetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PutMapping("/answer-sheets/{id}")
//    public Mono<ResponseEntity<AnswerSheetDTO>> updateAnswerSheet(
//        @PathVariable(value = "id", required = false) final Long id,
//        @Valid @RequestBody AnswerSheetDTO answerSheetDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to update AnswerSheet : {}, {}", id, answerSheetDTO);
//        if (answerSheetDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, answerSheetDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        return answerSheetRepository
//            .existsById(id)
//            .flatMap(exists -> {
//                if (!exists) {
//                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
//                }
//
//                return answerSheetService
//                    .save(answerSheetDTO)
//                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
//                    .map(result ->
//                        ResponseEntity
//                            .ok()
//                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//                            .body(result)
//                    );
//            });
//    }

    /**
     * {@code PATCH  /answer-sheets/:id} : Partial updates given fields of an existing answerSheet, field will ignore if it is null
     *
     * @param id the id of the answerSheetDTO to save.
     * @param answerSheetDTO the answerSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerSheetDTO,
     * or with status {@code 400 (Bad Request)} if the answerSheetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the answerSheetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the answerSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PatchMapping(value = "/answer-sheets/{id}", consumes = { "application/json", "application/merge-patch+json" })
//    public Mono<ResponseEntity<AnswerSheetDTO>> partialUpdateAnswerSheet(
//        @PathVariable(value = "id", required = false) final Long id,
//        @NotNull @RequestBody AnswerSheetDTO answerSheetDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update AnswerSheet partially : {}, {}", id, answerSheetDTO);
//        if (answerSheetDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, answerSheetDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        return answerSheetRepository
//            .existsById(id)
//            .flatMap(exists -> {
//                if (!exists) {
//                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
//                }
//
//                Mono<AnswerSheetDTO> result = answerSheetService.partialUpdate(answerSheetDTO);
//
//                return result
//                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
//                    .map(res ->
//                        ResponseEntity
//                            .ok()
//                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
//                            .body(res)
//                    );
//            });
//    }

    /**
     * {@code GET  /answer-sheets} : get all the answerSheets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answerSheets in body.
     */
//    @GetMapping("/answer-sheets")
//    public Mono<ResponseEntity<List<AnswerSheetDTO>>> getAllAnswerSheets(Pageable pageable, ServerHttpRequest request) {
//        log.debug("REST request to get a page of AnswerSheets");
//        return answerSheetService
//            .countAll()
//            .zipWith(answerSheetService.findAll(pageable).collectList())
//            .map(countWithEntities -> {
//                return ResponseEntity
//                    .ok()
//                    .headers(
//                        PaginationUtil.generatePaginationHttpHeaders(
//                            UriComponentsBuilder.fromHttpRequest(request),
//                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
//                        )
//                    )
//                    .body(countWithEntities.getT2());
//            });
//    }

    /**
     * {@code GET  /answer-sheets/:id} : get the "id" answerSheet.
     *
     * @param id the id of the answerSheetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerSheetDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("@answerSecurity.hasPermissionToGet(#id)")
    @GetMapping("/answer-sheets/{id}")
    public Mono<ResponseEntity<AnswerSheetDTO>> getAnswerSheet(@PathVariable Long id) {
        log.debug("REST request to get AnswerSheet : {}", id);
        Mono<AnswerSheetDTO> answerSheetDTO = answerSheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerSheetDTO);
    }

    /**
     * {@code DELETE  /answer-sheets/:id} : delete the "id" answerSheet.
     *
     * @param id the id of the answerSheetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
//    @DeleteMapping("/answer-sheets/{id}")
//    @ResponseStatus(code = HttpStatus.NO_CONTENT)
//    public Mono<ResponseEntity<Void>> deleteAnswerSheet(@PathVariable Long id) {
//        log.debug("REST request to delete AnswerSheet : {}", id);
//        return answerSheetService
//            .delete(id)
//            .map(result ->
//                ResponseEntity
//                    .noContent()
//                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//                    .build()
//            );
//    }
}
