package openproject.where42.groupFriend;

import lombok.RequiredArgsConstructor;
import openproject.where42.token.TokenService;
import openproject.where42.util.Define;
import openproject.where42.exception.customException.RegisteredFriendException;
import openproject.where42.group.GroupRepository;
import openproject.where42.member.MemberRepository;
import openproject.where42.member.MemberService;
import openproject.where42.member.entity.Member;
import openproject.where42.util.response.Response;
import openproject.where42.util.response.ResponseMsg;
import openproject.where42.util.response.ResponseWithData;
import openproject.where42.util.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupFriendApiController {

	private final GroupFriendService groupFriendService;
	private final GroupFriendRepository groupFriendRepository;
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final TokenService tokenService;

	// 검색을 통한 친구 등록, 기본 그룹에 등록
	@PostMapping(Define.WHERE42_VERSION_PATH + "/groupFriend")
	public ResponseEntity createFriend(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestParam String friendName, @RequestParam String img) {
		String token42 = tokenService.getToken(res, key);
		Member member = memberService.findBySessionWithToken(req, token42);
		if (memberRepository.checkFriendByMemberIdAndName(member.getId(), friendName))
			throw new RegisteredFriendException();
		Long friendId = groupFriendService.saveFriend(friendName, img, member.getDefaultGroupId());
		return new ResponseEntity(ResponseWithData.res(StatusCode.CREATED, ResponseMsg.CREATE_GROUP_FRIEND, friendId), HttpStatus.CREATED);
	}

	// 해당 그룹에 포함되지 않는 친구 이름 목록 전체 반환
	@GetMapping(Define.WHERE42_VERSION_PATH + "/groupFriend/notIncludes/group/{groupId}")
	public List<String> getNotIncludeGroupFriendNames(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @PathVariable("groupId") Long groupId) {
		String token42 = tokenService.getToken(res, key);
		Member member = memberService.findBySessionWithToken(req, token42);
		return groupFriendRepository.notIncludeFriendByGroup(member, groupId);
	}

	// 해당 그룹에 포함되지 않은 친구들 중 선택된 친구들 일괄 추가, 세션 검사 안함. 저장은 됨 얘도 중복검사 하자
	@PostMapping(Define.WHERE42_VERSION_PATH + "/groupFriend/notIncludes/group/{groupId}")
	public ResponseEntity addFriendsToGroup(@PathVariable("groupId") Long groupId, @RequestBody List<String> friendNames) {
		groupFriendService.addFriendsToGroup(friendNames, groupId);
		return new ResponseEntity(Response.res(StatusCode.CREATED, ResponseMsg.ADD_FRIENDS_TO_GROUP), HttpStatus.CREATED);
	}

	// 해당 그룹에 포함된 친구 이름 목록 전체 반환, 세션 검사 안함
	@GetMapping(Define.WHERE42_VERSION_PATH + "/groupFriend/includes/group/{groupId}")
	public List<String> getIncludeGroupFriendNames(@PathVariable("groupId") Long groupId) {
		return groupFriendRepository.findGroupFriendsByGroupId(groupId);
	}

	// 해당 그룹에 포함된 친구들 중 선택된 친구들 일괄 삭제
	@PostMapping(Define.WHERE42_VERSION_PATH + "/groupFriend/includes/group/{groupId}")
	public ResponseEntity removeIncludeGroupFriends(@PathVariable("groupId") Long groupId, @RequestBody List<String> friendNames) {
		groupFriendService.deleteIncludeGroupFriends(groupId, friendNames);
		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_FRIENDS_FROM_GROUP), HttpStatus.OK);
	}

	// 기본 그룹 친구 이름 목록 반환
	@GetMapping(Define.WHERE42_VERSION_PATH + "/groupFriend/friendList")
	public List<String> getAllDefaultFriends(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
		String token42 = tokenService.getToken(res, key);
		Member member = memberService.findBySessionWithToken(req, token42);
		return groupFriendRepository.findGroupFriendsByGroupId(member.getDefaultGroupId());
	}

	// 기본 그룹을 포함한 모든 그룹에서 삭제
	@PostMapping(Define.WHERE42_VERSION_PATH + "/groupFriend/friendList") // 프론트 기본에서 삭제하는 경우와 사용자정의 그룹에서만 삭제하는 경우 필히 구분지어서 매핑 필
	public ResponseEntity deleteFriends(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestBody List<String> friendNames) {
		String token42 = tokenService.getToken(res, key);
		Member member = memberService.findBySessionWithToken(req, token42);
		groupFriendService.deleteFriends(member, friendNames);
		return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_GROUP_FRIENDS), HttpStatus.OK);
	}
}
