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
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.GroupMemberMapper;

/**
 * Integration tests for the {@link GroupMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GroupMemberResourceIT {

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

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
        GroupMember groupMember = new GroupMember().userLogin(DEFAULT_USER_LOGIN).groupId(DEFAULT_GROUP_ID).role(DEFAULT_ROLE);
        return groupMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupMember createUpdatedEntity(EntityManager em) {
        GroupMember groupMember = new GroupMember().userLogin(UPDATED_USER_LOGIN).groupId(UPDATED_GROUP_ID).role(UPDATED_ROLE);
        return groupMember;
    }

    @BeforeEach
    public void initTest() {
        groupMember = createEntity(em);
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
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.intValue())))
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
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }

    @Test
    @Transactional
    void getNonExistingGroupMember() throws Exception {
        // Get the groupMember
        restGroupMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }
}
