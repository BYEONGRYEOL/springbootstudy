package com.example.full.repository.member;

import com.example.full.entity.member.Member;
import com.example.full.entity.member.MemberRole;
import com.example.full.entity.member.Role;
import com.example.full.entity.member.RoleType;
import com.example.full.exception.MemberNotFoundException;
import com.example.full.repository.member.MemberRepository;
import com.example.full.repository.member.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@Transactional
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    private void clear(){
        em.flush();
        em.clear();
    }
    @Test
    void createAndReadTest(){
        //given
        Member member = createMember();

        //when
        memberRepository.save(member);
        clear();

        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest(){
        //given
        Member member = createMember();
        //when
        memberRepository.save(member);
        clear();
        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);

        assertThat(foundMember.getCreatedDate()).isNotNull();
        assertThat(foundMember.getModifiedDate()).isNotNull();
        assertThat(foundMember.getCreatedDate()).isEqualTo(foundMember.getModifiedDate());
    }

    @Test
    void updateTest(){
        //given
        String updatedNickname = "updated";
        Member member = memberRepository.save(createMember());
        clear();
        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        foundMember.updateNickname(updatedNickname);
        clear();
        //then
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(updatedMember.getNickname()).isEqualTo(updatedNickname);
    }
    @Test
    void deleteTest(){
        //given
        Member member = memberRepository.save(createMember());
        clear();
        //when
        memberRepository.delete(member);
        clear();
        //then
        assertThatThrownBy(()->memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new))
                .isInstanceOf(MemberNotFoundException.class);

    }

    @Test
    void findByEmailTest(){
        //given
        Member member = memberRepository.save(createMember());
        clear();
        //when
        Member foundMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);
        //then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findByNicknameTest(){
        //given
        Member member = memberRepository.save(createMember());
        clear();
        //when
        Member foundMember = memberRepository.findByNickname(member.getNickname()).orElseThrow(MemberNotFoundException::new);
        //then
        assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void uniqueEmailTest(){
        //given
        Member member = memberRepository.save(createMember("email1", "password1", "username", "nickname"));
        clear();

        //when then
        assertThatThrownBy(()-> memberRepository.save(createMember(member.getEmail(), "anotherpassword", "anotherusername", "anothernickname")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void existsByEmailTest(){
        //given
        Member member = memberRepository.save(createMember());
        clear();
        // when then
        assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsByEmail(member.getEmail()+"test")).isFalse();
    }

    @Test
    void existsByNicknameTest(){
        //given
        Member member = memberRepository.save(createMember());
        clear();
        //when then
        assertThat(memberRepository.existsByNickname(member.getNickname())).isTrue();
        assertThat(memberRepository.existsByNickname(member.getNickname() + "test")).isFalse();

    }

    @Test
    void memberRoleCascadePersistTest(){
        //given

        List<Role> roles = saveRoles();
        clear();
        // EntityManager.flush() 실행시 쓰기 지연 저장소에 저장되어있는 쿼리를 DB에 날려 저장한다.
        // EntityManager.clear() 메소드 실행시 영속성 컨텍스트에 캐싱된 엔티티를 삭제한다.
        // -> 다음 조회 쿼리를 할 때 영속성 컨텍스트에서 가져오는게 아닌 실제 DB에 접근하여 조회함.
        Member member = saveMemberWithRoles(roles);
        clear();
        //when

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        //아까 EntityManager가 쿼리문도 즉시 날아가도록 했고 영속성 컨텍스트에 캐싱된 엔티티들도 삭제했기때문에 여기서
        //1.Repository가 JpaRepository의 Query Creation 으로 동적으로 생성된 조회 쿼리문 실행
        //2.먼저 영속성 컨텍스트에 조회하는 엔티티가 있는지 확인, 영속성 컨텍스트의 캐시를 지웠으니 없음
        //3. 영속성 컨텍스트에 없다면 DB에 접근하여 조회한다.

        //위 과정을 확인하기 위해 디버깅해보면 아까 저장한 엔티티 클래스의 식별자id(pointer) 와 조회한 엔티티 클래스의 식별자id가 다르다.

        Set<MemberRole> memberRoles = foundMember.getRoles();
        //then
        assertThat(memberRoles.size()).isEqualTo(roles.size()); //영속성 테스트
        //내가 select한 member row 그러니까 Member 객체에 role 값이 내가 만들엇떤 것과 똑같은 값이 나오지 않을 수도 있다?



    }

    @Test
    void memberRoleCascadeDeleteTest(){
        //given
        List<RoleType> roleTypes = List.of(RoleType.class.getEnumConstants());
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();


        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<MemberRole> result = em.createQuery("select mr from MemberRole mr", MemberRole.class).getResultList();
        assertThat(result.size()).isZero();
    }

    private Member createMember(){
        return new Member("sbl1998@naver.com", "asdf", "서병렬", "지수조아", emptyList());
    }
    private Member createMember(String email, String password, String username,String nickname){
        return new Member(email, password, username, nickname, emptyList());
    }
    private Member createMemberWithRoles(List<Role> roles){
        return new Member("email", "password", "username", "nickname", roles);
    }

    List<Role> saveRoles(){
        List<RoleType> roleTypes = List.of(RoleType.class.getEnumConstants()); // Transient RoleType
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        return roleRepository.saveAll(roles); // Persistent RoleType
    }

    Member saveMemberWithRoles(List<Role> roles){
        return memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
    }
}
