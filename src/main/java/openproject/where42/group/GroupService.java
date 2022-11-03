package openproject.where42.group;

import lombok.RequiredArgsConstructor;
import openproject.where42.group.domain.Groups;
import openproject.where42.group.repository.GroupRepository;
import openproject.where42.member.domain.Member;
import openproject.where42.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createDefaultGroup(Member member, String groupName) {
        if (!(groupName.equalsIgnoreCase("기본") || groupName.equalsIgnoreCase("즐겨찾기"))) // 기본이나 즐겨찾기 아닌 다른 그룹 혹시 잘못 호출 시 예외 터트릴 건데 어디서 잡을 지 모르겠어서 일단 걍 리턴 나중에 에러처리 필요
            return Long.valueOf(0);
        return groupRepository.save(new Groups(groupName, member));
    }

    @Transactional // 기본적으로 false여서 안쓰면 false임
    public void saveGroup(String groupName, Long ownerId) {
        validateDuplicateGroupName(ownerId, groupName);
        Member owner = memberRepository.findById(ownerId);
        groupRepository.save(new Groups(groupName, owner));
    }

    @Transactional
    public boolean updateGroupName(Long groupId, String groupName) {
        Groups group = groupRepository.findById(groupId);

        try {
            validateDuplicateGroupName(group.getOwner().getId(), groupName);
        } catch (IllegalStateException e) {
            return false;
        }
        group.updateGroupName(groupName);
        return true; // 예외처리 더 알아보자
    }

    private void validateDuplicateGroupName(Long ownerId, String groupName) { // 여러곳에서 호출 시에 대한 에러 처리 필요 싱글톤 패턴 참고
        if (groupRepository.isGroupNameInOwner(ownerId, groupName))
            throw new IllegalStateException("이미 사용하고 있는 그룹 이름입니다.");
    }

    public List<Groups> findGroups(Long ownerId) {
        return groupRepository.findGroupsByOwnerId(ownerId);
    }

    @Transactional
    public void deleteByGroupId(Long groupId) {
        groupRepository.deleteByGroupId(groupId);
    }
}