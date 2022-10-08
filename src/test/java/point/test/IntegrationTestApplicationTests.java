package point.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import point.test.common.Const;
import point.test.domain.Member;
import point.test.repository.MemberRepository;
import point.test.service.MemberService;
import point.test.service.PointService;

import javax.transaction.Transactional;

@SpringBootTest
class IntegrationTestApplicationTests {

    @Autowired
    MemberService memberService;

    @Autowired
    PointService pointService;

    @Test
    @Transactional
    void 포인트_세이브_테스트() {


    }
    
    @Test
    @Transactional
    void 포인트_사용_테스트(){


    }
    

}
