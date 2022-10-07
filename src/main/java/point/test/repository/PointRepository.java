package point.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import point.test.domain.Point;

public interface PointRepository extends JpaRepository<Point,Long> {
}
