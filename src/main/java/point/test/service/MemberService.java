package point.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import point.test.domain.Member;
import point.test.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMember(Long id){
        return memberRepository.findById(id).orElse(null);
    }

    public void saveMember(Member member){
        memberRepository.save(member);
    }

}
