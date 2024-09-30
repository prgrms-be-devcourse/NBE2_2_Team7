//package com.hunmin.domain.repository;
//
//import com.hunmin.domain.entity.Member;
//import lombok.extern.log4j.Log4j2;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//import java.util.stream.IntStream;
//
//import static com.hunmin.domain.entity.MemberRole.ADMIN;
//import static com.hunmin.domain.entity.MemberRole.USER;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Log4j2
//class MemberRepositoryTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    public void testDataInsert() {
//        IntStream.rangeClosed(1, 10).forEach(i -> {
//            Member member = Member
//                    .builder()
//                    .memberId((long) i)
//                    .password("1234")
//                    .email("USER" + i + "@email.com")
//                    .nickname("USER" + i)
//                    .country("USER_COUNTRY" + i)
//                    .level("LEVEL" + i)
//                    .image("IMAGE" + i)
//                    .memberRole(i <= 8 ? USER : ADMIN)
//                    .build();
//            Member savedMember = memberRepository.save(member);
//
//            assertNotNull(savedMember);
//            if (i <= 8) {
//                assertEquals(savedMember.getMemberRole(), USER);
//            } else {
//                assertEquals(savedMember.getMemberRole(), ADMIN);
//            }
//        });
//        log.info("================ 데이터 삽입 완료 ================ : " + memberRepository.count());
//    }
//
//    @Test
//    void testFindByEmail() {
//        String email = "USER1@email.com";
//
//        Optional<Member> foundMember = memberRepository.findByEmail(email);
//
//        assertTrue(foundMember.isPresent(), "해당 이메일 존재: " + email);
//        foundMember.ifPresent(member -> {
//            assertEquals(email, member.getEmail());
//            log.info("파운드멤버: " + foundMember);
//            log.info("이메일: " + foundMember.get().getEmail());
//        });
//    }
//
//    @Test
//    void testFindByNickname() {
//        String nickname = "USER1";
//
//        Optional<Member> foundMember = memberRepository.findByNickname(nickname);
//
//        assertTrue(foundMember.isPresent(), "해당 닉네임 존재: " + nickname);
//        foundMember.ifPresent(member -> {
//            assertEquals(nickname, member.getNickname());
//            log.info("파운드멤버: " + foundMember);
//            log.info("닉네임: " + member.getNickname());
//        });
//    }
//
//    @Test
//    void testExistsByEmail() {
//        String email = "USER10@email.com";
//        boolean exists = memberRepository.existsByEmail(email);
//        assertTrue(exists);
//        log.info("존재하는 이메일 테스트: " + email);
//
//        String notExistEmail = "ADMIN10@email.com";
//        boolean doesNotExist = memberRepository.existsByEmail(notExistEmail);
//        assertFalse(doesNotExist);
//        log.info("존재하지 않는 이메일 테스트: " + notExistEmail);
//    }
//
//    @Test
//    void testExistsByNickname() {
//        String nickname = "USER1";
//        boolean exists = memberRepository.existsByNickname(nickname);
//        assertTrue(exists);
//        log.info("존재하는 닉네임 테스트: " + nickname);
//
//        String notExistNickname = "not_exist_nickname";
//        boolean doesNotExist = memberRepository.existsByNickname(notExistNickname);
//        assertFalse(doesNotExist);
//        log.info("존재하지 않는 닉네임 테스트: " + notExistNickname);
//    }
//}