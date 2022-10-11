package point.test.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @ApiOperation("포인트 사용/적립 내역")
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
    @ApiOperation("현재 포인트 총합")
    @GetMapping("/{memberId}/points/totals")
    public ResponseEntity<RestResponse> getPointsTotal(
            @PathVariable Long memberId){

        return ResponseEntity.ok(RestResponse.ok(pointService.getCurrentTotalPoint(memberId)));
    }
    @ApiOperation("포인트 사용 적립 API")
    @PostMapping("/{memberId}/points/save")
    public ResponseEntity<RestResponse> savePoint(
            @PathVariable Long memberId,
            @RequestBody PointForm pointForm){

        pointService.savePoint(memberId , pointForm.getAmount() , pointForm.getDesc());

        return ResponseEntity.ok(RestResponse.ok());
    }

    @ApiOperation("포인트 사용 API")
    @PostMapping("/{memberId}/points/using")
    public ResponseEntity<RestResponse> usingPoint(
            @PathVariable Long memberId,
            @RequestBody PointForm pointForm) {

        pointService.usingPoint(memberId, pointForm.getAmount(), pointForm.getDesc());

        return ResponseEntity.ok(RestResponse.ok());
    }
    @ApiOperation("포인트 사용 취소 API - 사용한 내역 기반")
    @PostMapping("/{memberId}/points/{pointId}/cancel")
    public ResponseEntity<RestResponse> usingCancelPoint(
            @PathVariable Long memberId
           ,@PathVariable Long pointId) {

        pointService.cancelPoint(memberId, pointId);

        return ResponseEntity.ok(RestResponse.ok());
    }

}
