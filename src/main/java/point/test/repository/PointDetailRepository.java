package point.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import point.test.domain.PointHistory;
import point.test.domain.PointHistoryDetail;

import java.util.List;

public interface PointDetailRepository extends JpaRepository<PointHistoryDetail,Long> {

    List<PointHistoryDetail> findAllByPointUseHistory(PointHistory pointHistory);
}
