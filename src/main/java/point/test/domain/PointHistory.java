package point.test.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import point.test.enums.PointKind;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member"}, callSuper = true)
public class PointHistory extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @Enumerated(EnumType.STRING)
    private PointKind kind;

    private Long amount; //실제 적립/사용 금액

    @Setter
    @JsonIgnore
    private Long useAmount; //실제 사용가능한 금액

    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="member_id")
    private Member member;

    @OneToMany(mappedBy = "pointUseHistory" , fetch = FetchType.LAZY)
    private List<PointHistoryDetail> pointUseHistories;

    @OneToMany(mappedBy = "pointSaveHistory" , fetch = FetchType.LAZY)
    private List<PointHistoryDetail> pointSaveHistories;


    @Builder
    public PointHistory(PointKind kind , String desc , Long amount , Long useAmount , Member member){
        this.kind=kind;
        this.desc=desc;
        this.amount=amount;
        this.useAmount=useAmount;
        this.member = member;
    }

}
