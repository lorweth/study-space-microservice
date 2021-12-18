package vn.vnedu.studyspace.exam_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.exam_store.domain.GroupMember;
import vn.vnedu.studyspace.exam_store.repository.GroupMemberRepository;
import vn.vnedu.studyspace.exam_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.GroupMemberMapper;

@Service
public class GroupMemberService {

    private final Logger log = LoggerFactory.getLogger(GroupMemberService.class);

    private final GroupMemberRepository groupMemberRepository;

    private final GroupMemberMapper groupMemberMapper;

    public GroupMemberService(GroupMemberRepository groupMemberRepository, GroupMemberMapper groupMemberMapper){
        this.groupMemberRepository = groupMemberRepository;
        this.groupMemberMapper = groupMemberMapper;
    }

    public GroupMemberDTO save(GroupMemberDTO groupMemberDTO) {
        log.debug("Request to save groupMember");
        GroupMember groupMember = groupMemberMapper.toEntity(groupMemberDTO);
        groupMember = groupMemberRepository.save(groupMember);
        return groupMemberMapper.toDto(groupMember);
    }

    public void delete(Long memberId) {
        log.debug("Request to delete groupMember");
        groupMemberRepository.deleteById(memberId);
    }
}
