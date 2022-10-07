package point.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import point.test.common.RestResponse;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/members")
public class PointController {

    @GetMapping("/{id}/points")
    public ResponseEntity<RestResponse> getPoints(){
        return ResponseEntity.ok(RestResponse.ok());
    }

    @GetMapping("/{id}/points/totals")
    public ResponseEntity<RestResponse> getPointsTotal(){
        return ResponseEntity.ok(RestResponse.ok());
    }

    @PostMapping("/{id}/points/save")
    public ResponseEntity<RestResponse> savePoint(){
        return ResponseEntity.ok(RestResponse.ok());
    }

    @PostMapping("/{id}/points/using")
    public ResponseEntity<RestResponse> usingPoint(){
        return ResponseEntity.ok(RestResponse.ok());
    }


}
