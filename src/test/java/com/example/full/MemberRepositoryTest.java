package com.example.full;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        List<RoleType> roleTypes = List.of(RoleType.class.getEnumConstants());
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roles));
        clear();
        //when

        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        List<MemberRole> memberRoles = List.of((MemberRole) foundMember.getRoles());

        //then
        assertThat(memberRoles.equals(roles)).isTrue();
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
}
