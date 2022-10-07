package point.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import point.test.Exception.ServiceException;
import point.test.domain.Member;
import point.test.domain.PointHistory;
import point.test.enums.PointKind;
import point.test.repository.PointRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PointService {

    private final PointRepository pointRepository;
    private final MemberService memberService;

    public Page<PointHistory> getPointHistory(Long memberId , PointKind kind ,Pageable pageable){

        Member member = memberService.getMember(memberId);
        return pointRepository.findAllByKindAndMemberOrderByCreatedDateDesc( kind , member , pageable);
    }

    public Long getTotalPoint(Long memberId){
        return getCurrentPoint(memberId);
    }

    public Long getCurrentPoint(Long memberId){
        List<PointHistory> pointSaveHistoryList = pointRepository.findAllPointCustomSql(memberId , PointKind.SAVE.toString());
        List<PointHistory> pointUsingHistoryList = pointRepository.findAllPointCustomSql(memberId , PointKind.USE.toString());
        return pointSaveHistoryList.stream().mapToLong(PointHistory::getAmount).sum() - pointUsingHistoryList.stream().mapToLong(PointHistory::getAmount).sum() ;
    }

    /**
     * 포인트 적립.
     * @param memberId
     * @param amount
     * @param desc
     */
    @Transactional
    public void savePoint(Long memberId, Long amount , String desc){

        if(amount <= 0){
            throw new ServiceException("포인트 적립은 0 또는 - 가 될 수 없습니다.");
        }

        Member member = memberService.getMember(memberId);

        log.info("member : {}" , member);

        //포인트 히스토리 기록.
        pointRepository.save(PointHistory.builder()
                .kind(PointKind.SAVE)
                .amount(amount)
                .desc(desc)
                .member(member)
                .build()
        );
    }

    /**
     * 포인트 사용.
     * @param memberId
     * @param amount
     * @param desc
     */
    @Transactional
    public void usingPoint(Long memberId, Long amount , String desc){


        Member member = memberService.getMember(memberId);

        Long getCurrentPoint = getCurrentPoint(memberId);

        log.info("getCurrentPoint : {}" , getCurrentPoint);

        if(amount <= 0){
            throw new ServiceException("포인트 적립은 0 또는 - 가 될 수 없습니다.");
        }

        if(amount > getCurrentPoint){
            throw new ServiceException("현재 가지고 있는 포인트가 부족합니다.");
        }

        log.info("member : {}" , member);

        //포인트 히스토리 기록.
        pointRepository.save(PointHistory.builder()
                .kind(PointKind.USE)
                .amount(amount)
                .desc(desc)
                .member(member)
                .build()
        );

//        List<PointHistory> pointHistoryList = pointRepository.findAllPointCustomSql(memberId);
//
//        log.info(pointHistoryList);




    }


}
