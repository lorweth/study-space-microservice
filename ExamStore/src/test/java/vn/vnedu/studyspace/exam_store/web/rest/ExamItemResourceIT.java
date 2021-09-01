package vn.vnedu.studyspace.exam_store.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import vn.vnedu.studyspace.exam_store.domain.ExamItem;
import vn.vnedu.studyspace.exam_store.repository.ExamItemRepository;
import vn.vnedu.studyspace.exam_store.service.dto.ExamItemDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.ExamItemMapper;

/**
 * Integration tests for the {@link ExamItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamItemResourceIT {

    private static final Integer DEFAULT_NUM_OF_QUESTION = 1;
    private static final Integer UPDATED_NUM_OF_QUESTION = 2;

    private static final String ENTITY_API_URL = "/api/exam-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamItemRepository examItemRepository;

    @Autowired
    private ExamItemMapper examItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamItemMockMvc;

    private ExamItem examItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamItem createEntity(EntityManager em) {
        ExamItem examItem = new ExamItem().numOfQuestion(DEFAULT_NUM_OF_QUESTION);
        return examItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamItem createUpdatedEntity(EntityManager em) {
        ExamItem examItem = new ExamItem().numOfQuestion(UPDATED_NUM_OF_QUESTION);
        return examItem;
    }

    @BeforeEach
    public void initTest() {
        examItem = createEntity(em);
    }

    @Test
    @Transactional
    void createExamItem() throws Exception {
        int databaseSizeBeforeCreate = examItemRepository.findAll().size();
        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);
        restExamItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeCreate + 1);
        ExamItem testExamItem = examItemList.get(examItemList.size() - 1);
        assertThat(testExamItem.getNumOfQuestion()).isEqualTo(DEFAULT_NUM_OF_QUESTION);
    }

    @Test
    @Transactional
    void createExamItemWithExistingId() throws Exception {
        // Create the ExamItem with an existing ID
        examItem.setId(1L);
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        int databaseSizeBeforeCreate = examItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumOfQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = examItemRepository.findAll().size();
        // set the field null
        examItem.setNumOfQuestion(null);

        // Create the ExamItem, which fails.
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        restExamItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamItems() throws Exception {
        // Initialize the database
        examItemRepository.saveAndFlush(examItem);

        // Get all the examItemList
        restExamItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].numOfQuestion").value(hasItem(DEFAULT_NUM_OF_QUESTION)));
    }

    @Test
    @Transactional
    void getExamItem() throws Exception {
        // Initialize the database
        examItemRepository.saveAndFlush(examItem);

        // Get the examItem
        restExamItemMockMvc
            .perform(get(ENTITY_API_URL_ID, examItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examItem.getId().intValue()))
            .andExpect(jsonPath("$.numOfQuestion").value(DEFAULT_NUM_OF_QUESTION));
    }

    @Test
    @Transactional
    void getNonExistingExamItem() throws Exception {
        // Get the examItem
        restExamItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExamItem() throws Exception {
        // Initialize the database
        examItemRepository.saveAndFlush(examItem);

        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();

        // Update the examItem
        ExamItem updatedExamItem = examItemRepository.findById(examItem.getId()).get();
        // Disconnect from session so that the updates on updatedExamItem are not directly saved in db
        em.detach(updatedExamItem);
        updatedExamItem.numOfQuestion(UPDATED_NUM_OF_QUESTION);
        ExamItemDTO examItemDTO = examItemMapper.toDto(updatedExamItem);

        restExamItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
        ExamItem testExamItem = examItemList.get(examItemList.size() - 1);
        assertThat(testExamItem.getNumOfQuestion()).isEqualTo(UPDATED_NUM_OF_QUESTION);
    }

    @Test
    @Transactional
    void putNonExistingExamItem() throws Exception {
        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();
        examItem.setId(count.incrementAndGet());

        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamItem() throws Exception {
        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();
        examItem.setId(count.incrementAndGet());

        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamItem() throws Exception {
        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();
        examItem.setId(count.incrementAndGet());

        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamItemWithPatch() throws Exception {
        // Initialize the database
        examItemRepository.saveAndFlush(examItem);

        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();

        // Update the examItem using partial update
        ExamItem partialUpdatedExamItem = new ExamItem();
        partialUpdatedExamItem.setId(examItem.getId());

        restExamItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamItem))
            )
            .andExpect(status().isOk());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
        ExamItem testExamItem = examItemList.get(examItemList.size() - 1);
        assertThat(testExamItem.getNumOfQuestion()).isEqualTo(DEFAULT_NUM_OF_QUESTION);
    }

    @Test
    @Transactional
    void fullUpdateExamItemWithPatch() throws Exception {
        // Initialize the database
        examItemRepository.saveAndFlush(examItem);

        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();

        // Update the examItem using partial update
        ExamItem partialUpdatedExamItem = new ExamItem();
        partialUpdatedExamItem.setId(examItem.getId());

        partialUpdatedExamItem.numOfQuestion(UPDATED_NUM_OF_QUESTION);

        restExamItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamItem))
            )
            .andExpect(status().isOk());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
        ExamItem testExamItem = examItemList.get(examItemList.size() - 1);
        assertThat(testExamItem.getNumOfQuestion()).isEqualTo(UPDATED_NUM_OF_QUESTION);
    }

    @Test
    @Transactional
    void patchNonExistingExamItem() throws Exception {
        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();
        examItem.setId(count.incrementAndGet());

        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamItem() throws Exception {
        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();
        examItem.setId(count.incrementAndGet());

        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamItem() throws Exception {
        int databaseSizeBeforeUpdate = examItemRepository.findAll().size();
        examItem.setId(count.incrementAndGet());

        // Create the ExamItem
        ExamItemDTO examItemDTO = examItemMapper.toDto(examItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamItem in the database
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamItem() throws Exception {
        // Initialize the database
        examItemRepository.saveAndFlush(examItem);

        int databaseSizeBeforeDelete = examItemRepository.findAll().size();

        // Delete the examItem
        restExamItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, examItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamItem> examItemList = examItemRepository.findAll();
        assertThat(examItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
