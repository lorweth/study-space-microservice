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
import vn.vnedu.studyspace.answer_store.repository.GroupTimeTableRepository;
import vn.vnedu.studyspace.answer_store.service.GroupTimeTableService;
import vn.vnedu.studyspace.answer_store.service.dto.GroupTimeTableDTO;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.vnedu.studyspace.answer_store.domain.GroupTimeTable}.
 */
@RestController
@RequestMapping("/api")
public class GroupTimeTableResource {

    private final Logger log = LoggerFactory.getLogger(GroupTimeTableResource.class);

    private static final String ENTITY_NAME = "answerStoreGroupTimeTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupTimeTableService groupTimeTableService;

    private final GroupTimeTableRepository groupTimeTableRepository;

    public GroupTimeTableResource(GroupTimeTableService groupTimeTableService, GroupTimeTableRepository groupTimeTableRepository) {
        this.groupTimeTableService = groupTimeTableService;
        this.groupTimeTableRepository = groupTimeTableRepository;
    }

    /**
     * {@code POST  /group-time-tables} : Create a new groupTimeTable.
     *
     * @param groupTimeTableDTO the groupTimeTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupTimeTableDTO, or with status {@code 400 (Bad Request)} if the groupTimeTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/group-time-tables")
    public Mono<ResponseEntity<GroupTimeTableDTO>> createGroupTimeTable(@Valid @RequestBody GroupTimeTableDTO groupTimeTableDTO)
        throws URISyntaxException {
        log.debug("REST request to save GroupTimeTable : {}", groupTimeTableDTO);
        if (groupTimeTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new groupTimeTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return groupTimeTableService
            .save(groupTimeTableDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/group-time-tables/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /group-time-tables/:id} : Updates an existing groupTimeTable.
     *
     * @param id the id of the groupTimeTableDTO to save.
     * @param groupTimeTableDTO the groupTimeTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupTimeTableDTO,
     * or with status {@code 400 (Bad Request)} if the groupTimeTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupTimeTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/group-time-tables/{id}")
    public Mono<ResponseEntity<GroupTimeTableDTO>> updateGroupTimeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GroupTimeTableDTO groupTimeTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GroupTimeTable : {}, {}", id, groupTimeTableDTO);
        if (groupTimeTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupTimeTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return groupTimeTableRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return groupTimeTableService
                        .save(groupTimeTableDTO)
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
     * {@code PATCH  /group-time-tables/:id} : Partial updates given fields of an existing groupTimeTable, field will ignore if it is null
     *
     * @param id the id of the groupTimeTableDTO to save.
     * @param groupTimeTableDTO the groupTimeTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupTimeTableDTO,
     * or with status {@code 400 (Bad Request)} if the groupTimeTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the groupTimeTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the groupTimeTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/group-time-tables/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<GroupTimeTableDTO>> partialUpdateGroupTimeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GroupTimeTableDTO groupTimeTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GroupTimeTable partially : {}, {}", id, groupTimeTableDTO);
        if (groupTimeTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupTimeTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return groupTimeTableRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<GroupTimeTableDTO> result = groupTimeTableService.partialUpdate(groupTimeTableDTO);

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
     * {@code GET  /group-time-tables} : get all the groupTimeTables.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupTimeTables in body.
     */
    @GetMapping("/group-time-tables")
    public Mono<ResponseEntity<List<GroupTimeTableDTO>>> getAllGroupTimeTables(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of GroupTimeTables");
        return groupTimeTableService
            .countAll()
            .zipWith(groupTimeTableService.findAll(pageable).collectList())
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
     * {@code GET  /group-time-tables/:id} : get the "id" groupTimeTable.
     *
     * @param id the id of the groupTimeTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupTimeTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/group-time-tables/{id}")
    public Mono<ResponseEntity<GroupTimeTableDTO>> getGroupTimeTable(@PathVariable Long id) {
        log.debug("REST request to get GroupTimeTable : {}", id);
        Mono<GroupTimeTableDTO> groupTimeTableDTO = groupTimeTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupTimeTableDTO);
    }

    /**
     * {@code DELETE  /group-time-tables/:id} : delete the "id" groupTimeTable.
     *
     * @param id the id of the groupTimeTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/group-time-tables/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGroupTimeTable(@PathVariable Long id) {
        log.debug("REST request to delete GroupTimeTable : {}", id);
        return groupTimeTableService
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
