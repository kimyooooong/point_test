package point.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import point.test.common.Const;
import point.test.domain.Member;
import point.test.repository.MemberRepository;
import point.test.service.MemberService;

import javax.transaction.Transactional;

@SpringBootTest
class ServiceTestApplicationTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @Transactional
    void contextLoads() {

       Member member = memberService.getMember(Const.TEST_MEMBER_ID);
       System.out.println(member);



    }

}
