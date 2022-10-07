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

    @GetMapping("/{id}/points")
    public ResponseEntity<RestResponse> getPoints(
            @PathVariable Long id,
            @RequestParam(value = "kind") PointKind kind,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size") Integer size

    ){
        //PageRequest 에선 offset 이 0 부터 들어가므로  page -1 .
        return ResponseEntity.ok(RestResponse.ok(pointService.getPointHistory(id , kind , PageRequest.of(page-1 , size ))));
    }

    @GetMapping("/{id}/points/totals")
    public ResponseEntity<RestResponse> getPointsTotal(
            @PathVariable Long id){

        id = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        return ResponseEntity.ok(RestResponse.ok(pointService.getTotalPoint(id)));
    }

    @PostMapping("/{id}/points/save")
    public ResponseEntity<RestResponse> savePoint(
            @PathVariable Long id,
            @RequestBody PointForm pointForm){

        id = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        pointService.savePoint(id , pointForm.getAmount() , pointForm.getDesc());

        return ResponseEntity.ok(RestResponse.ok());
    }

    @PostMapping("/{id}/points/using")
    public ResponseEntity<RestResponse> usingPoint(
            @PathVariable Long id,
            @RequestBody PointForm pointForm) {

        id = Const.TEST_MEMBER_ID; //멤버 아이디는 테스트아이디로 고정.

        pointService.usingPoint(id, pointForm.getAmount(), pointForm.getDesc());

        return ResponseEntity.ok(RestResponse.ok());
    }

}
