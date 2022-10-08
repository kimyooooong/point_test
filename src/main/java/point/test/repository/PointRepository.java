package point.test.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import point.test.domain.Member;
import point.test.domain.PointHistory;
import point.test.enums.PointKind;

import javax.transaction.Transactional;
import java.util.List;

public interface PointRepository extends JpaRepository<PointHistory,Long> {

    /**
     * 날짜 - 유효기간 1년 이하.
     * 적립한 포인트
     * 날짜 오래된 순.
     * @param id
     * @return
     */
    @Query(value = " SELECT * FROM POINT_HISTORY WHERE CREATED_DATE >= DATEADD('YEAR' , -1 , CURRENT_DATE) AND USE_AMOUNT != 0 AND MEMBER_ID = :id AND KIND = :kind ORDER BY CREATED_DATE" , nativeQuery = true)
    List<PointHistory> findAllPointCustomSql(@Param(value = "id")Long id , @Param(value = "kind" ) String kind);

    Page<PointHistory> findAllByKindAndMemberOrderByCreatedDateDesc(PointKind kind , Member member , Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE PointHistory SET useAmount = useAmount + :amount where pointId = :pointId")
    void updateUseAmountByPointId(@Param(value = "pointId") Long pointId  , @Param(value = "amount") Long amount);

}
