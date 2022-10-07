package point.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import point.test.domain.Member;

public interface MemberRepository extends JpaRepository<Member , Long> {

}
