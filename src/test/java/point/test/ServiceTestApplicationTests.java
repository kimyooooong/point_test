package point.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import point.test.common.Const;
import point.test.domain.Member;
import point.test.repository.MemberRepository;
import point.test.service.MemberService;
import point.test.service.PointService;

import javax.transaction.Transactional;

@SpringBootTest
class ServiceTestApplicationTests {

    @Autowired
    MemberService memberService;

    @Autowired
    PointService pointService;

    @Test
    @Transactional
    void 포인트_세이브_테스트() {
        System.out.println("===========포인트_세이브_테스트 시작============");

        Long amount = 50000L;

        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");
        Assertions.assertEquals(pointService.getTotalPoint(Const.TEST_MEMBER_ID) , amount);

        System.out.println("===========포인트_세이브_테스트 종료============");
    }

    @Test
    @Transactional
    void 포인트_사용_테스트(){

        System.out.println("===========포인트_사용_테스트 시작============");

        Long amount = 50000L;

        System.out.println("포인트 충전 전 : " + pointService.getTotalPoint(Const.TEST_MEMBER_ID) );

        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");

        System.out.println("포인트 사용 전 : " + pointService.getTotalPoint(Const.TEST_MEMBER_ID) );

        amount -= 25000;

        pointService.usingPoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트 사용");

        System.out.println("포인트 사용 후 : " + pointService.getTotalPoint(Const.TEST_MEMBER_ID) );

        Assertions.assertEquals(pointService.getTotalPoint(Const.TEST_MEMBER_ID) , amount);

        System.out.println("===========포인트_사용_테스트 종료============");

    }

    @Test
    @Transactional
    void 포인트_사용_취소_테스트(){
        System.out.println("===========포인트_사용_취소_테스트 시작============");














        System.out.println("===========포인트_사용_취소_테스트 종료============");
    }

}
