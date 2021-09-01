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
import org.springframework.util.Base64Utils;
import vn.vnedu.studyspace.answer_store.IntegrationTest;
import vn.vnedu.studyspace.answer_store.domain.TimeTable;
import vn.vnedu.studyspace.answer_store.repository.TimeTableRepository;
import vn.vnedu.studyspace.answer_store.service.EntityManager;
import vn.vnedu.studyspace.answer_store.service.dto.TimeTableDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.TimeTableMapper;

/**
 * Integration tests for the {@link TimeTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class TimeTableResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/time-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private TimeTableMapper timeTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TimeTable timeTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeTable createEntity(EntityManager em) {
        TimeTable timeTable = new TimeTable().title(DEFAULT_TITLE).time(DEFAULT_TIME).note(DEFAULT_NOTE).userLogin(DEFAULT_USER_LOGIN);
        return timeTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeTable createUpdatedEntity(EntityManager em) {
        TimeTable timeTable = new TimeTable().title(UPDATED_TITLE).time(UPDATED_TIME).note(UPDATED_NOTE).userLogin(UPDATED_USER_LOGIN);
        return timeTable;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TimeTable.class).block();
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
        timeTable = createEntity(em);
    }

    @Test
    void createTimeTable() throws Exception {
        int databaseSizeBeforeCreate = timeTableRepository.findAll().collectList().block().size();
        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate + 1);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTimeTable.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testTimeTable.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testTimeTable.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    void createTimeTableWithExistingId() throws Exception {
        // Create the TimeTable with an existing ID
        timeTable.setId(1L);
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        int databaseSizeBeforeCreate = timeTableRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().collectList().block().size();
        // set the field null
        timeTable.setUserLogin(null);

        // Create the TimeTable, which fails.
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTimeTables() {
        // Initialize the database
        timeTableRepository.save(timeTable).block();

        // Get all the timeTableList
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
            .value(hasItem(timeTable.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].time")
            .value(hasItem(DEFAULT_TIME.toString()))
            .jsonPath("$.[*].note")
            .value(hasItem(DEFAULT_NOTE.toString()))
            .jsonPath("$.[*].userLogin")
            .value(hasItem(DEFAULT_USER_LOGIN));
    }

    @Test
    void getTimeTable() {
        // Initialize the database
        timeTableRepository.save(timeTable).block();

        // Get the timeTable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, timeTable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(timeTable.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.time")
            .value(is(DEFAULT_TIME.toString()))
            .jsonPath("$.note")
            .value(is(DEFAULT_NOTE.toString()))
            .jsonPath("$.userLogin")
            .value(is(DEFAULT_USER_LOGIN));
    }

    @Test
    void getNonExistingTimeTable() {
        // Get the timeTable
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.save(timeTable).block();

        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();

        // Update the timeTable
        TimeTable updatedTimeTable = timeTableRepository.findById(timeTable.getId()).block();
        updatedTimeTable.title(UPDATED_TITLE).time(UPDATED_TIME).note(UPDATED_NOTE).userLogin(UPDATED_USER_LOGIN);
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(updatedTimeTable);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, timeTableDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTimeTable.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTimeTable.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testTimeTable.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    void putNonExistingTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, timeTableDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTimeTableWithPatch() throws Exception {
        // Initialize the database
        timeTableRepository.save(timeTable).block();

        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();

        // Update the timeTable using partial update
        TimeTable partialUpdatedTimeTable = new TimeTable();
        partialUpdatedTimeTable.setId(timeTable.getId());

        partialUpdatedTimeTable.time(UPDATED_TIME).note(UPDATED_NOTE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTimeTable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeTable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTimeTable.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTimeTable.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testTimeTable.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    void fullUpdateTimeTableWithPatch() throws Exception {
        // Initialize the database
        timeTableRepository.save(timeTable).block();

        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();

        // Update the timeTable using partial update
        TimeTable partialUpdatedTimeTable = new TimeTable();
        partialUpdatedTimeTable.setId(timeTable.getId());

        partialUpdatedTimeTable.title(UPDATED_TITLE).time(UPDATED_TIME).note(UPDATED_NOTE).userLogin(UPDATED_USER_LOGIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTimeTable.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeTable))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTimeTable.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testTimeTable.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testTimeTable.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    void patchNonExistingTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, timeTableDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().collectList().block().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTimeTable() {
        // Initialize the database
        timeTableRepository.save(timeTable).block();

        int databaseSizeBeforeDelete = timeTableRepository.findAll().collectList().block().size();

        // Delete the timeTable
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, timeTable.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TimeTable> timeTableList = timeTableRepository.findAll().collectList().block();
        assertThat(timeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
