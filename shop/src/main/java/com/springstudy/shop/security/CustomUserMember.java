package com.springstudy.shop.security;

import com.springstudy.shop.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class CustomUserMember extends User {
    private final Long id;
    private final String password;
    private final String address;

    // member.getEmail()이 User객체에서 Id역할: User의 Id는 유일성을 보장하는 필드이여함.
    public CustomUserMember(Member member, List<GrantedAuthority> authorities) {
        super(member.getEmail(), member.getPassword(), authorities);

        //User객체가 가지는 username과 password 이외에 id,getAddress,password 추가.
        this.id = member.getId();
        this.password = member.getPassword();
        this.address = member.getAddress();
    }
}
