package vn.vnedu.studyspace.answer_store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import vn.vnedu.studyspace.answer_store.IntegrationTest;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetRepository;
import vn.vnedu.studyspace.answer_store.service.EntityManager;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.AnswerSheetMapper;

/**
 * Integration tests for the {@link AnswerSheetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AnswerSheetResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Long DEFAULT_EXAM_ID = 1L;
    private static final Long UPDATED_EXAM_ID = 2L;

    private static final String ENTITY_API_URL = "/api/answer-sheets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnswerSheetRepository answerSheetRepository;

    @Autowired
    private AnswerSheetMapper answerSheetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AnswerSheet answerSheet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerSheet createEntity(EntityManager em) {
        AnswerSheet answerSheet = new AnswerSheet()
            .createdAt(DEFAULT_CREATED_AT)
            .endAt(DEFAULT_END_AT)
            .userLogin(DEFAULT_USER_LOGIN)
            .examId(DEFAULT_EXAM_ID);
        return answerSheet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerSheet createUpdatedEntity(EntityManager em) {
        AnswerSheet answerSheet = new AnswerSheet()
            .createdAt(UPDATED_CREATED_AT)
            .endAt(UPDATED_END_AT)
            .userLogin(UPDATED_USER_LOGIN)
            .examId(UPDATED_EXAM_ID);
        return answerSheet;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AnswerSheet.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        answerSheet = createEntity(em);
    }

    @Test
    void createAnswerSheet() throws Exception {
        int databaseSizeBeforeCreate = answerSheetRepository.findAll().collectList().block().size();
        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeCreate + 1);
        AnswerSheet testAnswerSheet = answerSheetList.get(answerSheetList.size() - 1);
        assertThat(testAnswerSheet.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAnswerSheet.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testAnswerSheet.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testAnswerSheet.getExamId()).isEqualTo(DEFAULT_EXAM_ID);
    }

    @Test
    void createAnswerSheetWithExistingId() throws Exception {
        // Create the AnswerSheet with an existing ID
        answerSheet.setId(1L);
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        int databaseSizeBeforeCreate = answerSheetRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerSheetRepository.findAll().collectList().block().size();
        // set the field null
        answerSheet.setCreatedAt(null);

        // Create the AnswerSheet, which fails.
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerSheetRepository.findAll().collectList().block().size();
        // set the field null
        answerSheet.setUserLogin(null);

        // Create the AnswerSheet, which fails.
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerSheetRepository.findAll().collectList().block().size();
        // set the field null
        answerSheet.setExamId(null);

        // Create the AnswerSheet, which fails.
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnswerSheets() {
        // Initialize the database
        answerSheetRepository.save(answerSheet).block();

        // Get all the answerSheetList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(answerSheet.getId().intValue()))
            .jsonPath("$.[*].createdAt")
            .value(hasItem(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.[*].endAt")
            .value(hasItem(DEFAULT_END_AT.toString()))
            .jsonPath("$.[*].userLogin")
            .value(hasItem(DEFAULT_USER_LOGIN))
            .jsonPath("$.[*].examId")
            .value(hasItem(DEFAULT_EXAM_ID.intValue()));
    }

    @Test
    void getAnswerSheet() {
        // Initialize the database
        answerSheetRepository.save(answerSheet).block();

        // Get the answerSheet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, answerSheet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(answerSheet.getId().intValue()))
            .jsonPath("$.createdAt")
            .value(is(DEFAULT_CREATED_AT.toString()))
            .jsonPath("$.endAt")
            .value(is(DEFAULT_END_AT.toString()))
            .jsonPath("$.userLogin")
            .value(is(DEFAULT_USER_LOGIN))
            .jsonPath("$.examId")
            .value(is(DEFAULT_EXAM_ID.intValue()));
    }

    @Test
    void getNonExistingAnswerSheet() {
        // Get the answerSheet
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAnswerSheet() throws Exception {
        // Initialize the database
        answerSheetRepository.save(answerSheet).block();

        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();

        // Update the answerSheet
        AnswerSheet updatedAnswerSheet = answerSheetRepository.findById(answerSheet.getId()).block();
        updatedAnswerSheet.createdAt(UPDATED_CREATED_AT).endAt(UPDATED_END_AT).userLogin(UPDATED_USER_LOGIN).examId(UPDATED_EXAM_ID);
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(updatedAnswerSheet);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, answerSheetDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
        AnswerSheet testAnswerSheet = answerSheetList.get(answerSheetList.size() - 1);
        assertThat(testAnswerSheet.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAnswerSheet.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testAnswerSheet.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testAnswerSheet.getExamId()).isEqualTo(UPDATED_EXAM_ID);
    }

    @Test
    void putNonExistingAnswerSheet() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();
        answerSheet.setId(count.incrementAndGet());

        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, answerSheetDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnswerSheet() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();
        answerSheet.setId(count.incrementAndGet());

        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnswerSheet() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();
        answerSheet.setId(count.incrementAndGet());

        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnswerSheetWithPatch() throws Exception {
        // Initialize the database
        answerSheetRepository.save(answerSheet).block();

        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();

        // Update the answerSheet using partial update
        AnswerSheet partialUpdatedAnswerSheet = new AnswerSheet();
        partialUpdatedAnswerSheet.setId(answerSheet.getId());

        partialUpdatedAnswerSheet.createdAt(UPDATED_CREATED_AT).examId(UPDATED_EXAM_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnswerSheet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerSheet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
        AnswerSheet testAnswerSheet = answerSheetList.get(answerSheetList.size() - 1);
        assertThat(testAnswerSheet.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAnswerSheet.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testAnswerSheet.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testAnswerSheet.getExamId()).isEqualTo(UPDATED_EXAM_ID);
    }

    @Test
    void fullUpdateAnswerSheetWithPatch() throws Exception {
        // Initialize the database
        answerSheetRepository.save(answerSheet).block();

        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();

        // Update the answerSheet using partial update
        AnswerSheet partialUpdatedAnswerSheet = new AnswerSheet();
        partialUpdatedAnswerSheet.setId(answerSheet.getId());

        partialUpdatedAnswerSheet.createdAt(UPDATED_CREATED_AT).endAt(UPDATED_END_AT).userLogin(UPDATED_USER_LOGIN).examId(UPDATED_EXAM_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnswerSheet.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerSheet))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
        AnswerSheet testAnswerSheet = answerSheetList.get(answerSheetList.size() - 1);
        assertThat(testAnswerSheet.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAnswerSheet.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testAnswerSheet.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testAnswerSheet.getExamId()).isEqualTo(UPDATED_EXAM_ID);
    }

    @Test
    void patchNonExistingAnswerSheet() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();
        answerSheet.setId(count.incrementAndGet());

        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, answerSheetDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnswerSheet() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();
        answerSheet.setId(count.incrementAndGet());

        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnswerSheet() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetRepository.findAll().collectList().block().size();
        answerSheet.setId(count.incrementAndGet());

        // Create the AnswerSheet
        AnswerSheetDTO answerSheetDTO = answerSheetMapper.toDto(answerSheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AnswerSheet in the database
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnswerSheet() {
        // Initialize the database
        answerSheetRepository.save(answerSheet).block();

        int databaseSizeBeforeDelete = answerSheetRepository.findAll().collectList().block().size();

        // Delete the answerSheet
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, answerSheet.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AnswerSheet> answerSheetList = answerSheetRepository.findAll().collectList().block();
        assertThat(answerSheetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
