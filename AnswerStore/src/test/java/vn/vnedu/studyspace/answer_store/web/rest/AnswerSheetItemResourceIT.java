package vn.vnedu.studyspace.answer_store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
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
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetItemRepository;
import vn.vnedu.studyspace.answer_store.service.EntityManager;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerSheetItemDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.AnswerSheetItemMapper;

/**
 * Integration tests for the {@link AnswerSheetItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AnswerSheetItemResourceIT {

    private static final Long DEFAULT_QUESTION_ID = 1L;
    private static final Long UPDATED_QUESTION_ID = 2L;

    private static final Long DEFAULT_ANSWER_ID = 1L;
    private static final Long UPDATED_ANSWER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/answer-sheet-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnswerSheetItemRepository answerSheetItemRepository;

    @Autowired
    private AnswerSheetItemMapper answerSheetItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AnswerSheetItem answerSheetItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerSheetItem createEntity(EntityManager em) {
        AnswerSheetItem answerSheetItem = new AnswerSheetItem().questionId(DEFAULT_QUESTION_ID).answerId(DEFAULT_ANSWER_ID);
        return answerSheetItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnswerSheetItem createUpdatedEntity(EntityManager em) {
        AnswerSheetItem answerSheetItem = new AnswerSheetItem().questionId(UPDATED_QUESTION_ID).answerId(UPDATED_ANSWER_ID);
        return answerSheetItem;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AnswerSheetItem.class).block();
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
        answerSheetItem = createEntity(em);
    }

    @Test
    void createAnswerSheetItem() throws Exception {
        int databaseSizeBeforeCreate = answerSheetItemRepository.findAll().collectList().block().size();
        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeCreate + 1);
        AnswerSheetItem testAnswerSheetItem = answerSheetItemList.get(answerSheetItemList.size() - 1);
        assertThat(testAnswerSheetItem.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testAnswerSheetItem.getAnswerId()).isEqualTo(DEFAULT_ANSWER_ID);
    }

    @Test
    void createAnswerSheetItemWithExistingId() throws Exception {
        // Create the AnswerSheetItem with an existing ID
        answerSheetItem.setId(1L);
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        int databaseSizeBeforeCreate = answerSheetItemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuestionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerSheetItemRepository.findAll().collectList().block().size();
        // set the field null
        answerSheetItem.setQuestionId(null);

        // Create the AnswerSheetItem, which fails.
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAnswerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerSheetItemRepository.findAll().collectList().block().size();
        // set the field null
        answerSheetItem.setAnswerId(null);

        // Create the AnswerSheetItem, which fails.
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAnswerSheetItems() {
        // Initialize the database
        answerSheetItemRepository.save(answerSheetItem).block();

        // Get all the answerSheetItemList
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
            .value(hasItem(answerSheetItem.getId().intValue()))
            .jsonPath("$.[*].questionId")
            .value(hasItem(DEFAULT_QUESTION_ID.intValue()))
            .jsonPath("$.[*].answerId")
            .value(hasItem(DEFAULT_ANSWER_ID.intValue()));
    }

    @Test
    void getAnswerSheetItem() {
        // Initialize the database
        answerSheetItemRepository.save(answerSheetItem).block();

        // Get the answerSheetItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, answerSheetItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(answerSheetItem.getId().intValue()))
            .jsonPath("$.questionId")
            .value(is(DEFAULT_QUESTION_ID.intValue()))
            .jsonPath("$.answerId")
            .value(is(DEFAULT_ANSWER_ID.intValue()));
    }

    @Test
    void getNonExistingAnswerSheetItem() {
        // Get the answerSheetItem
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAnswerSheetItem() throws Exception {
        // Initialize the database
        answerSheetItemRepository.save(answerSheetItem).block();

        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();

        // Update the answerSheetItem
        AnswerSheetItem updatedAnswerSheetItem = answerSheetItemRepository.findById(answerSheetItem.getId()).block();
        updatedAnswerSheetItem.questionId(UPDATED_QUESTION_ID).answerId(UPDATED_ANSWER_ID);
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(updatedAnswerSheetItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, answerSheetItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
        AnswerSheetItem testAnswerSheetItem = answerSheetItemList.get(answerSheetItemList.size() - 1);
        assertThat(testAnswerSheetItem.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testAnswerSheetItem.getAnswerId()).isEqualTo(UPDATED_ANSWER_ID);
    }

    @Test
    void putNonExistingAnswerSheetItem() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();
        answerSheetItem.setId(count.incrementAndGet());

        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, answerSheetItemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAnswerSheetItem() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();
        answerSheetItem.setId(count.incrementAndGet());

        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAnswerSheetItem() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();
        answerSheetItem.setId(count.incrementAndGet());

        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAnswerSheetItemWithPatch() throws Exception {
        // Initialize the database
        answerSheetItemRepository.save(answerSheetItem).block();

        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();

        // Update the answerSheetItem using partial update
        AnswerSheetItem partialUpdatedAnswerSheetItem = new AnswerSheetItem();
        partialUpdatedAnswerSheetItem.setId(answerSheetItem.getId());

        partialUpdatedAnswerSheetItem.answerId(UPDATED_ANSWER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnswerSheetItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerSheetItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
        AnswerSheetItem testAnswerSheetItem = answerSheetItemList.get(answerSheetItemList.size() - 1);
        assertThat(testAnswerSheetItem.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testAnswerSheetItem.getAnswerId()).isEqualTo(UPDATED_ANSWER_ID);
    }

    @Test
    void fullUpdateAnswerSheetItemWithPatch() throws Exception {
        // Initialize the database
        answerSheetItemRepository.save(answerSheetItem).block();

        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();

        // Update the answerSheetItem using partial update
        AnswerSheetItem partialUpdatedAnswerSheetItem = new AnswerSheetItem();
        partialUpdatedAnswerSheetItem.setId(answerSheetItem.getId());

        partialUpdatedAnswerSheetItem.questionId(UPDATED_QUESTION_ID).answerId(UPDATED_ANSWER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAnswerSheetItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAnswerSheetItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
        AnswerSheetItem testAnswerSheetItem = answerSheetItemList.get(answerSheetItemList.size() - 1);
        assertThat(testAnswerSheetItem.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testAnswerSheetItem.getAnswerId()).isEqualTo(UPDATED_ANSWER_ID);
    }

    @Test
    void patchNonExistingAnswerSheetItem() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();
        answerSheetItem.setId(count.incrementAndGet());

        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, answerSheetItemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAnswerSheetItem() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();
        answerSheetItem.setId(count.incrementAndGet());

        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAnswerSheetItem() throws Exception {
        int databaseSizeBeforeUpdate = answerSheetItemRepository.findAll().collectList().block().size();
        answerSheetItem.setId(count.incrementAndGet());

        // Create the AnswerSheetItem
        AnswerSheetItemDTO answerSheetItemDTO = answerSheetItemMapper.toDto(answerSheetItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(answerSheetItemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AnswerSheetItem in the database
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAnswerSheetItem() {
        // Initialize the database
        answerSheetItemRepository.save(answerSheetItem).block();

        int databaseSizeBeforeDelete = answerSheetItemRepository.findAll().collectList().block().size();

        // Delete the answerSheetItem
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, answerSheetItem.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AnswerSheetItem> answerSheetItemList = answerSheetItemRepository.findAll().collectList().block();
        assertThat(answerSheetItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
