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
import vn.vnedu.studyspace.answer_store.domain.GroupTimeTable;
import vn.vnedu.studyspace.answer_store.repository.GroupTimeTableRepository;
import vn.vnedu.studyspace.answer_store.service.EntityManager;
import vn.vnedu.studyspace.answer_store.service.dto.GroupTimeTableDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.GroupTimeTableMapper;

/**
 * Integration tests for the {@link GroupTimeTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class GroupTimeTableResourceIT {

    private static final Long DEFAULT_EXAM_ID = 1L;
    private static final Long UPDATED_EXAM_ID = 2L;

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/group-time-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupTimeTableRepository groupTimeTableRepository;

    @Autowired
    private GroupTimeTableMapper groupTimeTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private GroupTimeTable groupTimeTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupTimeTable createEntity(EntityManager em) {
        GroupTimeTable groupTimeTable = new GroupTimeTable()
            .examId(DEFAULT_EXAM_ID)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .groupId(DEFAULT_GROUP_ID)
            .note(DEFAULT_NOTE);
        return groupTimeTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupTimeTable createUpdatedEntity(EntityManager em) {
        GroupTimeTable groupTimeTable = new GroupTimeTable()
            .examId(UPDATED_EXAM_ID)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .groupId(UPDATED_GROUP_ID)
            .note(UPDATED_NOTE);
        return groupTimeTable;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(GroupTimeTable.class).block();
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
        groupTimeTable = createEntity(em);
    }

    @Test
    void createGroupTimeTable() throws Exception {
        int databaseSizeBeforeCreate = groupTimeTableRepository.findAll().collectList().block().size();
        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeCreate + 1);
        GroupTimeTable testGroupTimeTable = groupTimeTableList.get(groupTimeTableList.size() - 1);
        assertThat(testGroupTimeTable.getExamId()).isEqualTo(DEFAULT_EXAM_ID);
        assertThat(testGroupTimeTable.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testGroupTimeTable.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testGroupTimeTable.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testGroupTimeTable.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    void createGroupTimeTableWithExistingId() throws Exception {
        // Create the GroupTimeTable with an existing ID
        groupTimeTable.setId(1L);
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        int databaseSizeBeforeCreate = groupTimeTableRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkExamIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupTimeTableRepository.findAll().collectList().block().size();
        // set the field null
        groupTimeTable.setExamId(null);

        // Create the GroupTimeTable, which fails.
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStartAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupTimeTableRepository.findAll().collectList().block().size();
        // set the field null
        groupTimeTable.setStartAt(null);

        // Create the GroupTimeTable, which fails.
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEndAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupTimeTableRepository.findAll().collectList().block().size();
        // set the field null
        groupTimeTable.setEndAt(null);

        // Create the GroupTimeTable, which fails.
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkGroupIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupTimeTableRepository.findAll().collectList().block().size();
        // set the field null
        groupTimeTable.setGroupId(null);

        // Create the GroupTimeTable, which fails.
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllGroupTimeTables() {
        // Initialize the database
        groupTimeTableRepository.save(groupTimeTable).block();

        // Get all the groupTimeTableList
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
            .value(hasItem(groupTimeTable.getId().intValue()))
            .jsonPath("$.[*].examId")
            .value(hasItem(DEFAULT_EXAM_ID.intValue()))
            .jsonPath("$.[*].startAt")
            .value(hasItem(DEFAULT_START_AT.toString()))
            .jsonPath("$.[*].endAt")
            .value(hasItem(DEFAULT_END_AT.toString()))
            .jsonPath("$.[*].groupId")
            .value(hasItem(DEFAULT_GROUP_ID))
            .jsonPath("$.[*].note")
            .value(hasItem(DEFAULT_NOTE));
    }

    @Test
    void getGroupTimeTable() {
        // Initialize the database
        groupTimeTableRepository.save(groupTimeTable).block();

        // Get the groupTimeTable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, groupTimeTable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(groupTimeTable.getId().intValue()))
            .jsonPath("$.examId")
            .value(is(DEFAULT_EXAM_ID.intValue()))
            .jsonPath("$.startAt")
            .value(is(DEFAULT_START_AT.toString()))
            .jsonPath("$.endAt")
            .value(is(DEFAULT_END_AT.toString()))
            .jsonPath("$.groupId")
            .value(is(DEFAULT_GROUP_ID))
            .jsonPath("$.note")
            .value(is(DEFAULT_NOTE));
    }

    @Test
    void getNonExistingGroupTimeTable() {
        // Get the groupTimeTable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGroupTimeTable() throws Exception {
        // Initialize the database
        groupTimeTableRepository.save(groupTimeTable).block();

        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();

        // Update the groupTimeTable
        GroupTimeTable updatedGroupTimeTable = groupTimeTableRepository.findById(groupTimeTable.getId()).block();
        updatedGroupTimeTable
            .examId(UPDATED_EXAM_ID)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .groupId(UPDATED_GROUP_ID)
            .note(UPDATED_NOTE);
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(updatedGroupTimeTable);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, groupTimeTableDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
        GroupTimeTable testGroupTimeTable = groupTimeTableList.get(groupTimeTableList.size() - 1);
        assertThat(testGroupTimeTable.getExamId()).isEqualTo(UPDATED_EXAM_ID);
        assertThat(testGroupTimeTable.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testGroupTimeTable.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testGroupTimeTable.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testGroupTimeTable.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void putNonExistingGroupTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();
        groupTimeTable.setId(count.incrementAndGet());

        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, groupTimeTableDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGroupTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();
        groupTimeTable.setId(count.incrementAndGet());

        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGroupTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();
        groupTimeTable.setId(count.incrementAndGet());

        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGroupTimeTableWithPatch() throws Exception {
        // Initialize the database
        groupTimeTableRepository.save(groupTimeTable).block();

        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();

        // Update the groupTimeTable using partial update
        GroupTimeTable partialUpdatedGroupTimeTable = new GroupTimeTable();
        partialUpdatedGroupTimeTable.setId(groupTimeTable.getId());

        partialUpdatedGroupTimeTable.startAt(UPDATED_START_AT).groupId(UPDATED_GROUP_ID).note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroupTimeTable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupTimeTable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
        GroupTimeTable testGroupTimeTable = groupTimeTableList.get(groupTimeTableList.size() - 1);
        assertThat(testGroupTimeTable.getExamId()).isEqualTo(DEFAULT_EXAM_ID);
        assertThat(testGroupTimeTable.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testGroupTimeTable.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testGroupTimeTable.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testGroupTimeTable.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void fullUpdateGroupTimeTableWithPatch() throws Exception {
        // Initialize the database
        groupTimeTableRepository.save(groupTimeTable).block();

        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();

        // Update the groupTimeTable using partial update
        GroupTimeTable partialUpdatedGroupTimeTable = new GroupTimeTable();
        partialUpdatedGroupTimeTable.setId(groupTimeTable.getId());

        partialUpdatedGroupTimeTable
            .examId(UPDATED_EXAM_ID)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .groupId(UPDATED_GROUP_ID)
            .note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGroupTimeTable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupTimeTable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
        GroupTimeTable testGroupTimeTable = groupTimeTableList.get(groupTimeTableList.size() - 1);
        assertThat(testGroupTimeTable.getExamId()).isEqualTo(UPDATED_EXAM_ID);
        assertThat(testGroupTimeTable.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testGroupTimeTable.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testGroupTimeTable.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testGroupTimeTable.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    void patchNonExistingGroupTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();
        groupTimeTable.setId(count.incrementAndGet());

        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, groupTimeTableDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGroupTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();
        groupTimeTable.setId(count.incrementAndGet());

        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGroupTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = groupTimeTableRepository.findAll().collectList().block().size();
        groupTimeTable.setId(count.incrementAndGet());

        // Create the GroupTimeTable
        GroupTimeTableDTO groupTimeTableDTO = groupTimeTableMapper.toDto(groupTimeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(groupTimeTableDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GroupTimeTable in the database
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGroupTimeTable() {
        // Initialize the database
        groupTimeTableRepository.save(groupTimeTable).block();

        int databaseSizeBeforeDelete = groupTimeTableRepository.findAll().collectList().block().size();

        // Delete the groupTimeTable
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, groupTimeTable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<GroupTimeTable> groupTimeTableList = groupTimeTableRepository.findAll().collectList().block();
        assertThat(groupTimeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
