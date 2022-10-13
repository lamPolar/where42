package openproject.where42.groupMember;

import lombok.RequiredArgsConstructor;
import openproject.where42.group.domain.Groups;
import openproject.where42.group.repository.GroupRepository;
import openproject.where42.groupMember.domain.GroupMember;
import openproject.where42.groupMember.domain.GroupMemberInfo;
import openproject.where42.groupMember.repository.GroupMemberRepository;
import openproject.where42.member.domain.Member;
import openproject.where42.member.repository.MemberRepository;
import org.apache.catalina.Group;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupMemberService {
	private final MemberRepository memberRepository;
	private final GroupMemberRepository groupMemberRepository;
	private final GroupRepository groupRepository;

	// 친구 1명에 대한 그룹 추가
	@Transactional
	public void saveGroupMember(String friendName, Long groupId) {
		Groups group = groupRepository.findById(groupId);

		GroupMember groupMember = new GroupMember(friendName, group);
		groupMemberRepository.save(groupMember);
//		return groupMember.getId();
	}

	// 다중 친구 그룹 추가
	@Transactional
	public void multiSaveGroupMember(List<GroupMember> groupMemberList) {
		groupMemberRepository.multiSave(groupMemberList);
	}

	// 해당 친구가 포함되지 않은 그룹 목록 front 반환 ---> API
//	public List<String> notIncludeGroupByFriend(Member member, String friendName) {
//		return groupMemberRepository.notIncludeGroupByFriend(member, friendName);
//	}

	// 해당 그룹에 포함되지 않는 친구 목록 front 반환 ---> API
	public List<String> notIncludeFriendByGroup(Member member, Long groupId) {
		return groupMemberRepository.notIncludeFriendByGroup(member, groupId);
	}

	@Transactional
//	public void deleteGroupMember(Long groupId, String friendName) {
	public void deleteGroupMember(GroupMember groupMember) {
//		Groups group = groupRepository.findById(groupId);
//		GroupMember groupMember = groupMemberRepository.findByName(group, friendName); --> GroupMember타입으로 못 받아올 경우
		groupMemberRepository.deleteGroupMember(groupMember);
	}

	@Transactional
	public void deleteGroupMembers(List<GroupMember> groupMembers) {
		groupMemberRepository.deleteGroupMembers(groupMembers);
	}

	// 친구가 해당된 모든 그룹에서 삭제하기 --> 이거 어디서 불리는지 명확하지가 않아서 일단 보류
	@Transactional
	public void deleteFriendsGroupByName(Member member, String friendName) {
		groupMemberRepository.deleteFriendsGroupByName(member, friendName);
	}

	public List<GroupMemberInfo> findGroupMemberInfo(List<String> nameList) {
		List<GroupMemberInfo> result = null;
		for (String i: nameList)
			result.add(new GroupMemberInfo(memberRepository.findByName(i)));
		return result;
	}

}

//	private void validateDuplicateGroupMember(Groups group, String friend_name) {
//		for (GroupMember gm: group.getGroupMembers()){
//			if (gm.getFriendName() == friend_name)
//				throw new IllegalStateException("이미 등록된 친구입니다.");
//		}
//	}
