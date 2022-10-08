package point.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Member extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

//    @Setter
//    private Long point;

    @JsonManagedReference
    @OneToMany(mappedBy = "member" , fetch = FetchType.LAZY)
    private List<PointHistory> points = new ArrayList<>();
}
