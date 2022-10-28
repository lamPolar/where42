package openproject.where42.member;

import lombok.RequiredArgsConstructor;
import openproject.where42.group.GroupService;
import openproject.where42.group.domain.Groups;
import openproject.where42.groupFriend.GroupFriendService;
import openproject.where42.groupFriend.domain.GroupFriendInfo;
import openproject.where42.member.domain.Member;
import openproject.where42.member.dto.LocateForm;
import openproject.where42.member.dto.MemberGroupInfo;
import openproject.where42.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final GroupService groupService;

    @Transactional
    public void createMember(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void updatePersonalMsg(Long memberId, String msg) {
        Member member = memberRepository.findById(memberId);

        member.updatePersonalMsg(msg);
    }

    @Transactional
    public void updateLocate(Long memberId, LocateForm form) {
        Member member = memberRepository.findById(memberId);

        member.getLocate().updateLocate(form.getPlanet(), form.getFloor(), form.getCluster(), form.getSpot());
    }

    public List<MemberGroupInfo> findAllGroupFriendsInfo(Long memberId) {
        List<MemberGroupInfo> groupList = new ArrayList<MemberGroupInfo>();
        List<Groups> groups = groupService.findGroups(memberId);
//        for (Groups g : groups) // 그룹하나 당 groupInfo 만들어서 리스트에 추가해서 반환
//            groupList.add(new MemberGroupInfo(g, groupFriendService.findAllGroupFriendNameByGroupId(g.getId())));
        return groupList;
    }

    public List<GroupFriendInfo> findAllFriendsInfo(Long memberId) {
        Member member = memberRepository.findById(memberId);
//        return groupFriendService.findAllFriendsInfo(member.getDefaultGroup()); // 기본 그룹 id 보내주면 기본 그룹에 있는 친구들 정보 싹다 정리해서 반환
        return null;
    }
}