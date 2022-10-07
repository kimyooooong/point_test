-- 최초 유저 생성
insert into member(CREATED_DATE , MODIFIED_DATE) values (NOW() , NOW());select * from member;

select * from point_history where member_id = 1