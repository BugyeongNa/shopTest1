package com.springstudy.shop.entity;

import com.springstudy.shop.constant.Role;
import com.springstudy.shop.dto.MemberDTO;
import lombok.*;
import org.hibernate.annotations.GeneratorType;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.persistence.*;

@Entity
@Table(name="member")
@Setter@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "role" )
public class Member extends BaseEntity{// 등록자,수정자,등록일,수정일
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy= GenerationType.AUTO)// autoincrement
    private Long id;

    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;

    // 클라이언에서 넘겨온 정보중 암호화 작업을 한후 Entity에 전달
    public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setAddress(memberDTO.getAddress());

        // 암호화
        String password = passwordEncoder.encode(memberDTO.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        //member.setRole(Role.ADMIN);

        return member;
    }

}
