package openproject.where42.group;

import lombok.RequiredArgsConstructor;
import openproject.where42.util.Define;
import openproject.where42.member.entity.Member;
import openproject.where42.util.response.Response;
import openproject.where42.util.response.ResponseWithData;
import openproject.where42.util.response.ResponseMsg;
import openproject.where42.util.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupApiController {
    private final GroupService groupService;

    // 커스텀 그룹 생성
    @PostMapping(Define.WHERE42_VERSION_PATH + "/group")
    public ResponseEntity createCustomGroup(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key, @RequestParam("groupName") String groupName) {
        Member owner = groupService.findOwnerBySession(req, res, key);
        Long groupId = groupService.createCustomGroup(groupName, owner);
        return new ResponseEntity(ResponseWithData.res(StatusCode.CREATED, ResponseMsg.CREATE_GROUP, groupId), HttpStatus.CREATED);
    }

    // 기본 그룹 제외 그룹 목록 반환 (그룹 관리)
    @GetMapping(Define.WHERE42_VERSION_PATH + "/group")
    public List<GroupDto> getGroupsExceptDefault(HttpServletRequest req, HttpServletResponse res, @CookieValue(value = "ID", required = false) String key) {
        Member member = groupService.findOwnerBySession(req, res, key);
        List<Groups> groups = groupService.findAllGroupsExceptDefault(member.getId());
        List<GroupDto> result = new ArrayList<>();
        result.add(new GroupDto(member.getStarredGroupId(), "즐겨찾기"));
        for (Groups g : groups)
            result.add(new GroupDto(g.getId(), g.getGroupName()));
        return result;
    }

    // 커스텀 그룹 이름 수정
    @PostMapping(Define.WHERE42_VERSION_PATH + "/group/{groupId}")
    public ResponseEntity updateGroupName(@PathVariable("groupId") Long groupId, @RequestParam("changeName") String changeName) {
        groupService.updateGroupName(groupId, changeName);
        return new ResponseEntity(ResponseWithData.res(StatusCode.OK, ResponseMsg.CHANGE_GROUP_NAME, groupId), HttpStatus.OK);
    }

    // 세션 만료되어도 저장 됨
    @DeleteMapping(Define.WHERE42_VERSION_PATH + "/group/{groupId}")
    public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteByGroupId(groupId);
        return new ResponseEntity(Response.res(StatusCode.OK, ResponseMsg.DELETE_GROUP), HttpStatus.OK);
    }
}