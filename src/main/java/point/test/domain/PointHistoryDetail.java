package point.test.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryDetail extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointDetailId;

    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="point_use_id")
    private PointHistory pointUseHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="point_save_id")
    private PointHistory pointSaveHistory;


    @Builder
    public PointHistoryDetail(Long amount , PointHistory pointUseHistory , PointHistory pointSaveHistory){
        this.amount= amount;
        this.pointUseHistory = pointUseHistory;
        this.pointSaveHistory = pointSaveHistory;
    }

}
