package point.test.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import point.test.enums.PointKind;

import javax.persistence.*;

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

    private Long amount;

    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="member_id")
    @JsonBackReference
    private Member member;

    @Builder
    public PointHistory(PointKind kind , String desc , Long amount , Member member){
        this.kind=kind;
        this.desc=desc;
        this.amount=amount;
        this.member = member;
    }

}
