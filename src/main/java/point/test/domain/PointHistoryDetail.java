package point.test.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"pointUseHistory", "pointSaveHistory"}, callSuper = true)
public class PointHistoryDetail extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointDetailId;

    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name ="point_use_id")
    private PointHistory pointUseHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name ="point_save_id")
    private PointHistory pointSaveHistory;


    @Builder
    public PointHistoryDetail(Long amount , PointHistory pointUseHistory , PointHistory pointSaveHistory){
        this.amount= amount;
        this.pointUseHistory = pointUseHistory;
        this.pointSaveHistory = pointSaveHistory;
    }

}
