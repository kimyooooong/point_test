package point.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import point.test.common.Const;
import point.test.service.MemberService;
import point.test.service.PointService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class ServiceTestApplicationTests {

    @Autowired
    MemberService memberService;

    @Autowired
    PointService pointService;

    @Autowired
    EntityManager entityManager;

    @Test
    void 포인트_세이브_테스트() {
        System.out.println("===========포인트_세이브_테스트 시작============");

        Long amount = 50000L;

        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");
        Assertions.assertEquals(pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) , amount);

        System.out.println("===========포인트_세이브_테스트 종료============");
    }

    @Test
    void 포인트_사용_테스트(){

        System.out.println("===========포인트_사용_테스트 시작============");

        Long amount = 50000L;

        System.out.println("포인트 충전 전 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");

        System.out.println("포인트 사용 전 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        amount -= 25000;

        pointService.usingPoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트 사용");

        System.out.println("포인트 사용 후 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        Assertions.assertEquals(pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) , amount);

        System.out.println("===========포인트_사용_테스트 종료============");

    }

    @Test
    void 포인트_사용_취소_테스트(){
        System.out.println("===========포인트_사용_취소_테스트 시작============");

        Long amount = 1000000L;

        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");
        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");
        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");
        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");
        pointService.savePoint(Const.TEST_MEMBER_ID , amount , "공짜 포인트");

        System.out.println("포인트 충전 후 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        Long usingAmount = 1500000L;

        pointService.usingPoint(Const.TEST_MEMBER_ID , usingAmount , "공짜 포인트 사용");  // 포인트 사용아이디 6.
        pointService.usingPoint(Const.TEST_MEMBER_ID , usingAmount , "공짜 포인트 사용");  // 포인트 사용아이디 7.

        System.out.println("포인트 사용 후 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        pointService.cancelPoint(Const.TEST_MEMBER_ID , 6L);

        System.out.println("포인트 사용 취소 1 후 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        pointService.cancelPoint(Const.TEST_MEMBER_ID , 7L);

        System.out.println("포인트 사용 취소 2 후 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) );

        //사용전 포인트와 현재 포인트 체크.
        Assertions.assertEquals(pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) , 5000000L);

        System.out.println("===========포인트_사용_취소_테스트 종료============");
    }

}
