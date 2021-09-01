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
import vn.vnedu.studyspace.exam_store.domain.QuestionGroup;
import vn.vnedu.studyspace.exam_store.repository.QuestionGroupRepository;
import vn.vnedu.studyspace.exam_store.service.dto.QuestionGroupDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.QuestionGroupMapper;

/**
 * Integration tests for the {@link QuestionGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/question-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionGroupRepository questionGroupRepository;

    @Autowired
    private QuestionGroupMapper questionGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionGroupMockMvc;

    private QuestionGroup questionGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionGroup createEntity(EntityManager em) {
        QuestionGroup questionGroup = new QuestionGroup().name(DEFAULT_NAME).groupId(DEFAULT_GROUP_ID).userLogin(DEFAULT_USER_LOGIN);
        return questionGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionGroup createUpdatedEntity(EntityManager em) {
        QuestionGroup questionGroup = new QuestionGroup().name(UPDATED_NAME).groupId(UPDATED_GROUP_ID).userLogin(UPDATED_USER_LOGIN);
        return questionGroup;
    }

    @BeforeEach
    public void initTest() {
        questionGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionGroup() throws Exception {
        int databaseSizeBeforeCreate = questionGroupRepository.findAll().size();
        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);
        restQuestionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionGroup testQuestionGroup = questionGroupList.get(questionGroupList.size() - 1);
        assertThat(testQuestionGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestionGroup.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testQuestionGroup.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void createQuestionGroupWithExistingId() throws Exception {
        // Create the QuestionGroup with an existing ID
        questionGroup.setId(1L);
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        int databaseSizeBeforeCreate = questionGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionGroupRepository.findAll().size();
        // set the field null
        questionGroup.setName(null);

        // Create the QuestionGroup, which fails.
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        restQuestionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionGroupRepository.findAll().size();
        // set the field null
        questionGroup.setUserLogin(null);

        // Create the QuestionGroup, which fails.
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        restQuestionGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuestionGroups() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        // Get all the questionGroupList
        restQuestionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    @Transactional
    void getQuestionGroup() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        // Get the questionGroup
        restQuestionGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, questionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingQuestionGroup() throws Exception {
        // Get the questionGroup
        restQuestionGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionGroup() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();

        // Update the questionGroup
        QuestionGroup updatedQuestionGroup = questionGroupRepository.findById(questionGroup.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionGroup are not directly saved in db
        em.detach(updatedQuestionGroup);
        updatedQuestionGroup.name(UPDATED_NAME).groupId(UPDATED_GROUP_ID).userLogin(UPDATED_USER_LOGIN);
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(updatedQuestionGroup);

        restQuestionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionGroupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
        QuestionGroup testQuestionGroup = questionGroupList.get(questionGroupList.size() - 1);
        assertThat(testQuestionGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionGroup.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testQuestionGroup.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingQuestionGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();
        questionGroup.setId(count.incrementAndGet());

        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionGroupDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();
        questionGroup.setId(count.incrementAndGet());

        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();
        questionGroup.setId(count.incrementAndGet());

        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionGroupWithPatch() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();

        // Update the questionGroup using partial update
        QuestionGroup partialUpdatedQuestionGroup = new QuestionGroup();
        partialUpdatedQuestionGroup.setId(questionGroup.getId());

        partialUpdatedQuestionGroup.name(UPDATED_NAME);

        restQuestionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
        QuestionGroup testQuestionGroup = questionGroupList.get(questionGroupList.size() - 1);
        assertThat(testQuestionGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionGroup.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testQuestionGroup.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdateQuestionGroupWithPatch() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();

        // Update the questionGroup using partial update
        QuestionGroup partialUpdatedQuestionGroup = new QuestionGroup();
        partialUpdatedQuestionGroup.setId(questionGroup.getId());

        partialUpdatedQuestionGroup.name(UPDATED_NAME).groupId(UPDATED_GROUP_ID).userLogin(UPDATED_USER_LOGIN);

        restQuestionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionGroup))
            )
            .andExpect(status().isOk());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
        QuestionGroup testQuestionGroup = questionGroupList.get(questionGroupList.size() - 1);
        assertThat(testQuestionGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestionGroup.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testQuestionGroup.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();
        questionGroup.setId(count.incrementAndGet());

        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionGroupDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();
        questionGroup.setId(count.incrementAndGet());

        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionGroup() throws Exception {
        int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();
        questionGroup.setId(count.incrementAndGet());

        // Create the QuestionGroup
        QuestionGroupDTO questionGroupDTO = questionGroupMapper.toDto(questionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionGroup() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        int databaseSizeBeforeDelete = questionGroupRepository.findAll().size();

        // Delete the questionGroup
        restQuestionGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionGroup.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionGroup> questionGroupList = questionGroupRepository.findAll();
        assertThat(questionGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
