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
import vn.vnedu.studyspace.answer_store.domain.GroupMember;
import vn.vnedu.studyspace.answer_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.answer_store.service.EntityManager;
import vn.vnedu.studyspace.answer_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.answer_store.service.mapper.GroupMemberMapper;

/**
 * Integration tests for the {@link GroupMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class GroupMemberResourceIT {

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROLE = 0;
    private static final Integer UPDATED_ROLE = 1;

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

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
    private WebTestClient webTestClient;

    private GroupMember groupMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupMember createEntity(EntityManager em) {
        GroupMember groupMember = new GroupMember().userLogin(DEFAULT_USER_LOGIN).role(DEFAULT_ROLE).groupId(DEFAULT_GROUP_ID);
        return groupMember;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupMember createUpdatedEntity(EntityManager em) {
        GroupMember groupMember = new GroupMember().userLogin(UPDATED_USER_LOGIN).role(UPDATED_ROLE).groupId(UPDATED_GROUP_ID);
        return groupMember;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(GroupMember.class).block();
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
        groupMember = createEntity(em);
    }

    @Test
    void getAllGroupMembersAsStream() {
        // Initialize the database
        groupMemberRepository.save(groupMember).block();

        List<GroupMember> groupMemberList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(GroupMemberDTO.class)
            .getResponseBody()
            .map(groupMemberMapper::toEntity)
            .filter(groupMember::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(groupMemberList).isNotNull();
        assertThat(groupMemberList).hasSize(1);
        GroupMember testGroupMember = groupMemberList.get(0);
        assertThat(testGroupMember.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testGroupMember.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testGroupMember.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
    }

    @Test
    void getAllGroupMembers() {
        // Initialize the database
        groupMemberRepository.save(groupMember).block();

        // Get all the groupMemberList
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
            .value(hasItem(groupMember.getId().intValue()))
            .jsonPath("$.[*].userLogin")
            .value(hasItem(DEFAULT_USER_LOGIN))
            .jsonPath("$.[*].role")
            .value(hasItem(DEFAULT_ROLE))
            .jsonPath("$.[*].groupId")
            .value(hasItem(DEFAULT_GROUP_ID.intValue()));
    }

    @Test
    void getGroupMember() {
        // Initialize the database
        groupMemberRepository.save(groupMember).block();

        // Get the groupMember
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, groupMember.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(groupMember.getId().intValue()))
            .jsonPath("$.userLogin")
            .value(is(DEFAULT_USER_LOGIN))
            .jsonPath("$.role")
            .value(is(DEFAULT_ROLE))
            .jsonPath("$.groupId")
            .value(is(DEFAULT_GROUP_ID.intValue()));
    }

    @Test
    void getNonExistingGroupMember() {
        // Get the groupMember
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }
}
