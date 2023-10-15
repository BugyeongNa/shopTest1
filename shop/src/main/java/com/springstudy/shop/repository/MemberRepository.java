package com.springstudy.shop.repository;


import com.springstudy.shop.entity.Member;
import com.springstudy.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    interface OrderRepository extends JpaRepository<Order, Long> {

    }
}
