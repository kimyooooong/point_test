package point.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import point.test.common.Const;
import point.test.enums.PointKind;
import point.test.form.PointForm;
import point.test.service.MemberService;
import point.test.service.PointService;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class IntegrationTestApplicationTests {

    @Autowired
    MemberService memberService;

    @Autowired
    PointService pointService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @Test
    void API_포인트_적립_테스트() throws Exception{

        System.out.println("===========API_포인트_적립_테스트 시작============");

        System.out.println("적립 전 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));
        
        PointForm pointForm = new PointForm();
        pointForm.setAmount(100000L);
        pointForm.setDesc("공짜 포인트 적립");

        ResultActions resultActions = mockMvc.perform(post("/members/"+Const.TEST_MEMBER_ID+"/points/save")
                        .content(objectMapper.writeValueAsString(pointForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());

        resultActions.andExpect(status().isOk());

        System.out.println("적립 후 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));
        
        System.out.println("===========API_포인트_적립_테스트 종료============");

        Assertions.assertEquals(pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) , 100000L);

    }


    @Test
    void API_포인트_사용_테스트() throws Exception{

        System.out.println("===========API_포인트_사용_테스트 시작============");

        System.out.println("적립 전 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        pointService.savePoint(Const.TEST_MEMBER_ID , 100000L , "공짜 포인트 적립.");

        System.out.println("적립 후 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        PointForm pointForm = new PointForm();
        pointForm.setAmount(100000L);
        pointForm.setDesc("공짜 포인트 사용");

        ResultActions resultActions = mockMvc.perform(post("/members/"+Const.TEST_MEMBER_ID+"/points/using")
                        .content(objectMapper.writeValueAsString(pointForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());

        resultActions.andExpect(status().isOk());

        System.out.println("사용 후 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        System.out.println("===========API_포인트_사용_테스트 종료============");

        Assertions.assertEquals(pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) , 0L);

    }


    @Test
    void API_포인트_사용_취소_테스트() throws Exception{

        System.out.println("===========API_포인트_사용_테스트 시작============");


        System.out.println("적립 전 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        //10번 포인트 적립.
        IntStream.range(0,10).forEach(c-> pointService.savePoint(Const.TEST_MEMBER_ID , 100000L , "공짜 포인트 적립."));

        System.out.println("적립 후 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        //포인트 사용
        pointService.usingPoint(Const.TEST_MEMBER_ID , 358342L , "포인트 사용.");  //포인트 아이디 11
        pointService.usingPoint(Const.TEST_MEMBER_ID , 358342L , "포인트 사용.");  //포인트 아이디 12
        pointService.usingPoint(Const.TEST_MEMBER_ID , 258342L , "포인트 사용.");  //포인트 아이디 13

        System.out.println("사용 후 포인트 : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        ResultActions resultActions = mockMvc.perform(post("/members/"+Const.TEST_MEMBER_ID+"/points/"+ 11 + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());

        resultActions.andExpect(status().isOk());

        System.out.println("사용 취소 후 포인트 ( 포인트 아이디 11 ) : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));

        resultActions = mockMvc.perform(post("/members/"+Const.TEST_MEMBER_ID+"/points/"+ 12 + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());

        resultActions.andExpect(status().isOk());

        System.out.println("사용 취소 후 포인트 ( 포인트 아이디 12 ) : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));


        resultActions = mockMvc.perform(post("/members/"+Const.TEST_MEMBER_ID+"/points/"+ 13 + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());

        resultActions.andExpect(status().isOk());

        System.out.println("사용 취소 후 포인트 ( 포인트 아이디 13 ) : " + pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID));


        Assertions.assertEquals(pointService.getCurrentTotalPoint(Const.TEST_MEMBER_ID) , 1000000L);

        System.out.println("===========API_포인트_사용_테스트 종료============");
    }

    @Test
    void API_포인트_사용_적립_내역_테스트() throws Exception{

        System.out.println("===========API_포인트_사용_적립_내역_테스트 시작============");


        //10번 포인트 적립.
        IntStream.range(0,10).forEach(c-> pointService.savePoint(Const.TEST_MEMBER_ID , 100000L , "공짜 포인트 적립."));

        ResultActions resultActions = mockMvc.perform(get("/members/"+Const.TEST_MEMBER_ID+"/points")
                        .queryParam("kind" , String.valueOf(PointKind.SAVE))
                        .queryParam("page" , String.valueOf(1))
                        .queryParam("size" , String.valueOf(10))

                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
                       // .andDo(print());

        resultActions.andExpect(status().isOk());
        MvcResult result = resultActions.andReturn();
        System.out.println(result.getResponse().getContentAsString());


        //포인트 사용.
        pointService.usingPoint(Const.TEST_MEMBER_ID , 328342L , "포인트 사용.");  //포인트 아이디 11
        pointService.usingPoint(Const.TEST_MEMBER_ID , 358342L , "포인트 사용.");  //포인트 아이디 12
        pointService.usingPoint(Const.TEST_MEMBER_ID , 258342L , "포인트 사용.");  //포인트 아이디 13

        resultActions = mockMvc.perform(get("/members/"+Const.TEST_MEMBER_ID+"/points")
                        .queryParam("kind" , String.valueOf(PointKind.USE))
                        .queryParam("page" , String.valueOf(1))
                        .queryParam("size" , String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        resultActions.andExpect(status().isOk());
        result = resultActions.andReturn();
        System.out.println(result.getResponse().getContentAsString());


        System.out.println("===========API_포인트_사용_적립_내역_테스트 종료============");

    }
    


}
