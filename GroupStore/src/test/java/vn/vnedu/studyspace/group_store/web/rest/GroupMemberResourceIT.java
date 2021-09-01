package vn.vnedu.studyspace.group_store.web.rest;

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
import vn.vnedu.studyspace.group_store.IntegrationTest;
import vn.vnedu.studyspace.group_store.domain.GroupMember;
import vn.vnedu.studyspace.group_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.service.mapper.GroupMemberMapper;

/**
 * Integration tests for the {@link GroupMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupMemberResourceIT {

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROLE = 0;
    private static final Integer UPDATED_ROLE = 1;

    private static final String ENTITY_API_URL = "/api/group-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMemberMockMvc;

    private GroupMember groupMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupMember createEntity(EntityManager em) {
        GroupMember groupMember = new GroupMember().userLogin(DEFAULT_USER_LOGIN).role(DEFAULT_ROLE);
        return groupMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupMember createUpdatedEntity(EntityManager em) {
        GroupMember groupMember = new GroupMember().userLogin(UPDATED_USER_LOGIN).role(UPDATED_ROLE);
        return groupMember;
    }

    @BeforeEach
    public void initTest() {
        groupMember = createEntity(em);
    }

    @Test
    @Transactional
    void createGroupMember() throws Exception {
        int databaseSizeBeforeCreate = groupMemberRepository.findAll().size();
        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);
        restGroupMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeCreate + 1);
        GroupMember testGroupMember = groupMemberList.get(groupMemberList.size() - 1);
        assertThat(testGroupMember.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testGroupMember.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    void createGroupMemberWithExistingId() throws Exception {
        // Create the GroupMember with an existing ID
        groupMember.setId(1L);
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        int databaseSizeBeforeCreate = groupMemberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupMemberRepository.findAll().size();
        // set the field null
        groupMember.setUserLogin(null);

        // Create the GroupMember, which fails.
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        restGroupMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupMemberRepository.findAll().size();
        // set the field null
        groupMember.setRole(null);

        // Create the GroupMember, which fails.
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        restGroupMemberMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGroupMembers() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        // Get all the groupMemberList
        restGroupMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }

    @Test
    @Transactional
    void getGroupMember() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        // Get the groupMember
        restGroupMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, groupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupMember.getId().intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingGroupMember() throws Exception {
        // Get the groupMember
        restGroupMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGroupMember() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();

        // Update the groupMember
        GroupMember updatedGroupMember = groupMemberRepository.findById(groupMember.getId()).get();
        // Disconnect from session so that the updates on updatedGroupMember are not directly saved in db
        em.detach(updatedGroupMember);
        updatedGroupMember.userLogin(UPDATED_USER_LOGIN).role(UPDATED_ROLE);
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(updatedGroupMember);

        restGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupMemberDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
        GroupMember testGroupMember = groupMemberList.get(groupMemberList.size() - 1);
        assertThat(testGroupMember.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testGroupMember.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void putNonExistingGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();
        groupMember.setId(count.incrementAndGet());

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groupMemberDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();
        groupMember.setId(count.incrementAndGet());

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();
        groupMember.setId(count.incrementAndGet());

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMemberMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupMemberWithPatch() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();

        // Update the groupMember using partial update
        GroupMember partialUpdatedGroupMember = new GroupMember();
        partialUpdatedGroupMember.setId(groupMember.getId());

        partialUpdatedGroupMember.role(UPDATED_ROLE);

        restGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroupMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupMember))
            )
            .andExpect(status().isOk());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
        GroupMember testGroupMember = groupMemberList.get(groupMemberList.size() - 1);
        assertThat(testGroupMember.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testGroupMember.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void fullUpdateGroupMemberWithPatch() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();

        // Update the groupMember using partial update
        GroupMember partialUpdatedGroupMember = new GroupMember();
        partialUpdatedGroupMember.setId(groupMember.getId());

        partialUpdatedGroupMember.userLogin(UPDATED_USER_LOGIN).role(UPDATED_ROLE);

        restGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroupMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroupMember))
            )
            .andExpect(status().isOk());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
        GroupMember testGroupMember = groupMemberList.get(groupMemberList.size() - 1);
        assertThat(testGroupMember.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testGroupMember.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    void patchNonExistingGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();
        groupMember.setId(count.incrementAndGet());

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, groupMemberDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();
        groupMember.setId(count.incrementAndGet());

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = groupMemberRepository.findAll().size();
        groupMember.setId(count.incrementAndGet());

        // Create the GroupMember
        GroupMemberDTO groupMemberDTO = groupMemberMapper.toDto(groupMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groupMemberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GroupMember in the database
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroupMember() throws Exception {
        // Initialize the database
        groupMemberRepository.saveAndFlush(groupMember);

        int databaseSizeBeforeDelete = groupMemberRepository.findAll().size();

        // Delete the groupMember
        restGroupMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, groupMember.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GroupMember> groupMemberList = groupMemberRepository.findAll();
        assertThat(groupMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
