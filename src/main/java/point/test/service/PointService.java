package point.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import point.test.Exception.ServiceException;
import point.test.domain.Member;
import point.test.domain.PointHistory;
import point.test.domain.PointHistoryDetail;
import point.test.enums.PointKind;
import point.test.repository.PointRepository;
import point.test.repository.PointDetailRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PointService {

    private final PointRepository pointRepository;

    private final PointDetailRepository pointDetailRepository;

    private final MemberService memberService;

    private final EntityManager entityManager;

    /**
     * 포인트 사용/적립 내역
     * @param memberId
     * @param kind
     * @param pageable
     * @return
     */
    public Page<PointHistory> getPointHistory(Long memberId , PointKind kind ,Pageable pageable){

        Member member = memberService.getMember(memberId);
        return pointRepository.findAllByKindAndMemberOrderByCreatedDateDesc( kind , member , pageable);
    }

    /**
     * 현재 토탈 포인트.
     * @param memberId
     * @return
     */
    public Long getCurrentTotalPoint(Long memberId){
        //총 사용가능한 금액전부더해서 리턴.
        return pointRepository.findAllPointCustomSql(memberId , PointKind.SAVE.toString()).stream().mapToLong(PointHistory::getUseAmount).sum();
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

        //포인트 히스토리 기록.
        pointRepository.save(PointHistory.builder()
                .kind(PointKind.SAVE)
                .amount(amount)
                .useAmount(amount)
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

        Long getCurrentPoint = getCurrentTotalPoint(memberId);

        if(amount <= 0){
            throw new ServiceException("포인트 적립은 0 또는 - 가 될 수 없습니다.");
        }

        if(amount > getCurrentPoint){
            throw new ServiceException("현재 가지고 있는 포인트가 부족합니다.");
        }

        //포인트 히스토리 기록.
        PointHistory usePointHistory = pointRepository.save(PointHistory.builder()
                .kind(PointKind.USE)
                .amount(amount)
                .desc(desc)
                .member(member)
                .build()
        );

        //적립한 포인트를 적립 순으로 가져온다.
        List<PointHistory> pointHistoryList = pointRepository.findAllPointCustomSql(memberId  , PointKind.SAVE.toString());

        List<PointHistory> changeHistoryList = new ArrayList<>();
        List<PointHistoryDetail> pointUseHistories = new ArrayList<>();
        for ( PointHistory pointHistory : pointHistoryList){

            if(amount == 0){
                break;
            }

            var current = 0L;

            //현재 차감해야하는 금액이 같거나 크면 적립포인트 만큼 빼고 해당 적립포인트값을 0으로 세팅.
            if( amount >= pointHistory.getUseAmount()){
                amount -= pointHistory.getUseAmount();
                current = pointHistory.getUseAmount();
                pointHistory.setUseAmount(0L);
            //차감해아는 금액이 더 작으면 해당적립포인트에서 차감금액을 빼고 차감금액을 0으로 세팅.
            } else {
                pointHistory.setUseAmount(pointHistory.getUseAmount() - amount);
                current = amount;
                amount = 0L;
            }

            pointUseHistories.add(
                    PointHistoryDetail.builder()
                    .amount(current)
                    .pointUseHistory(usePointHistory)
                    .pointSaveHistory(pointHistory)
                    .build());
            changeHistoryList.add(pointHistory);
        }
        //변경된 차감금액리스트만 업데이트.
        pointRepository.saveAll(changeHistoryList);
        //차감한 금액 정보 기록 ( 롤백용 )
        pointDetailRepository.saveAll(pointUseHistories);
    }

    /**
     * 포인트 취소
     * @param memberId
     * @param pointId
     */
    public void cancelPoint(Long memberId , Long pointId){

        Optional<PointHistory> pointHistoryOptional = pointRepository.findById(pointId);



        //포인트 사용 정보가 없다면.
        if(pointHistoryOptional.isEmpty()){
            throw new ServiceException("포인트 사용 정보가 존재하지 않습니다.");

        } else {

            // 포인트 사용이아니라면.
            if(!pointHistoryOptional.get().getKind().equals(PointKind.USE)){
                throw new ServiceException("포인트 사용 정보가 아닙니다.");
            }


            log.info(pointHistoryOptional.get());

            PointHistory pointHistory = pointHistoryOptional.get();

            //사용 내역을 가져 옴.
            List<PointHistoryDetail> pointHistoryDetails = pointDetailRepository.findAllByPointUseHistory(pointHistory);

            //사용 내역 기반으로 포인트 적립부분을 다시 롤백.
            pointHistoryDetails.forEach(c-> pointRepository.updateUseAmountByPointId(c.getPointSaveHistory().getPointId() , c.getAmount()));

            entityManager.flush();
            entityManager.clear();
        }

    }

}
