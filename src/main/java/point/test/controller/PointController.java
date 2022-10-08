package point.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import point.test.common.Const;
import point.test.common.RestResponse;
import point.test.enums.PointKind;
import point.test.form.PointForm;
import point.test.service.PointService;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/members")
public class PointController {

    private final PointService pointService;

    @GetMapping("/{memberId}/points")
    public ResponseEntity<RestResponse> getPoints(
            @PathVariable Long memberId,
            @RequestParam(value = "kind") PointKind kind,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size

    ){
        //PageRequest 에선 offset 이 0 부터 들어가므로  page -1 .
        return ResponseEntity.ok(RestResponse.ok(pointService.getPointHistory(memberId , kind , PageRequest.of(page-1 , size ))));
    }

    @GetMapping("/{memberId}/points/totals")
    public ResponseEntity<RestResponse> getPointsTotal(
            @PathVariable Long memberId){

        memberId = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        return ResponseEntity.ok(RestResponse.ok(pointService.getTotalPoint(memberId)));
    }

    @PostMapping("/{memberId}/points/save")
    public ResponseEntity<RestResponse> savePoint(
            @PathVariable Long memberId,
            @RequestBody PointForm pointForm){

        memberId = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        pointService.savePoint(memberId , pointForm.getAmount() , pointForm.getDesc());

        return ResponseEntity.ok(RestResponse.ok());
    }

    @PostMapping("/{memberId}/points/using")
    public ResponseEntity<RestResponse> usingPoint(
            @PathVariable Long memberId,
            @RequestBody PointForm pointForm) {

        memberId = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        pointService.usingPoint(memberId, pointForm.getAmount(), pointForm.getDesc());

        return ResponseEntity.ok(RestResponse.ok());
    }

    @PostMapping("/{memberId}/points/{pointId}/cancel")
    public ResponseEntity<RestResponse> usingCancelPoint(
            @PathVariable Long memberId
            ,@PathVariable Long pointId) {

        memberId = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        pointService.cancelPoint(memberId, pointId);

        return ResponseEntity.ok(RestResponse.ok());
    }

}
