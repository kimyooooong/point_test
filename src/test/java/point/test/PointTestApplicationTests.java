package point.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import point.test.domain.Member;
import point.test.repository.MemberRepository;
import point.test.service.MemberService;

@SpringBootTest
class PointTestApplicationTests {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    void contextLoads() {

        memberRepository.save(new Member());



    }

}
