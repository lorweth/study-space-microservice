package vn.vnedu.studyspace.exam_store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.IntegrationTest;
import vn.vnedu.studyspace.exam_store.domain.Exam;
import vn.vnedu.studyspace.exam_store.repository.ExamRepository;
import vn.vnedu.studyspace.exam_store.service.dto.ExamDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.ExamMapper;

/**
 * Integration tests for the {@link ExamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURATION = 5;
    private static final Integer UPDATED_DURATION = 6;

    private static final Integer DEFAULT_MIX = 0;
    private static final Integer UPDATED_MIX = 1;

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/exams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamMockMvc;

    private Exam exam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exam createEntity(EntityManager em) {
        Exam exam = new Exam()
            .name(DEFAULT_NAME)
            .startAt(DEFAULT_START_AT)
            .endAt(DEFAULT_END_AT)
            .duration(DEFAULT_DURATION)
            .mix(DEFAULT_MIX)
            .groupId(DEFAULT_GROUP_ID)
            .userLogin(DEFAULT_USER_LOGIN);
        return exam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exam createUpdatedEntity(EntityManager em) {
        Exam exam = new Exam()
            .name(UPDATED_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .duration(UPDATED_DURATION)
            .mix(UPDATED_MIX)
            .groupId(UPDATED_GROUP_ID)
            .userLogin(UPDATED_USER_LOGIN);
        return exam;
    }

    @BeforeEach
    public void initTest() {
        exam = createEntity(em);
    }

    @Test
    @Transactional
    void createExam() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();
        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);
        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate + 1);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExam.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testExam.getEndAt()).isEqualTo(DEFAULT_END_AT);
        assertThat(testExam.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testExam.getMix()).isEqualTo(DEFAULT_MIX);
        assertThat(testExam.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testExam.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void createExamWithExistingId() throws Exception {
        // Create the Exam with an existing ID
        exam.setId(1L);
        ExamDTO examDTO = examMapper.toDto(exam);

        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setName(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setDuration(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMixIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setMix(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(exam);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExams() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList
        restExamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].endAt").value(hasItem(DEFAULT_END_AT.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].mix").value(hasItem(DEFAULT_MIX)))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get the exam
        restExamMockMvc
            .perform(get(ENTITY_API_URL_ID, exam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exam.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.endAt").value(DEFAULT_END_AT.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.mix").value(DEFAULT_MIX))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingExam() throws Exception {
        // Get the exam
        restExamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam
        Exam updatedExam = examRepository.findById(exam.getId()).get();
        // Disconnect from session so that the updates on updatedExam are not directly saved in db
        em.detach(updatedExam);
        updatedExam
            .name(UPDATED_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .duration(UPDATED_DURATION)
            .mix(UPDATED_MIX)
            .groupId(UPDATED_GROUP_ID)
            .userLogin(UPDATED_USER_LOGIN);
        ExamDTO examDTO = examMapper.toDto(updatedExam);

        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExam.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testExam.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testExam.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testExam.getMix()).isEqualTo(UPDATED_MIX);
        assertThat(testExam.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testExam.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        exam.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        exam.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        exam.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamWithPatch() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam using partial update
        Exam partialUpdatedExam = new Exam();
        partialUpdatedExam.setId(exam.getId());

        partialUpdatedExam
            .name(UPDATED_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .duration(UPDATED_DURATION)
            .mix(UPDATED_MIX)
            .groupId(UPDATED_GROUP_ID)
            .userLogin(UPDATED_USER_LOGIN);

        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExam.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExam))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExam.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testExam.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testExam.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testExam.getMix()).isEqualTo(UPDATED_MIX);
        assertThat(testExam.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testExam.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateExamWithPatch() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam using partial update
        Exam partialUpdatedExam = new Exam();
        partialUpdatedExam.setId(exam.getId());

        partialUpdatedExam
            .name(UPDATED_NAME)
            .startAt(UPDATED_START_AT)
            .endAt(UPDATED_END_AT)
            .duration(UPDATED_DURATION)
            .mix(UPDATED_MIX)
            .groupId(UPDATED_GROUP_ID)
            .userLogin(UPDATED_USER_LOGIN);

        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExam.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExam))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExam.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testExam.getEndAt()).isEqualTo(UPDATED_END_AT);
        assertThat(testExam.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testExam.getMix()).isEqualTo(UPDATED_MIX);
        assertThat(testExam.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testExam.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        exam.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        exam.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        exam.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(exam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        int databaseSizeBeforeDelete = examRepository.findAll().size();

        // Delete the exam
        restExamMockMvc
            .perform(delete(ENTITY_API_URL_ID, exam.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
