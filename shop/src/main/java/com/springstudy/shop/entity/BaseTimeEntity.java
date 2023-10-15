package com.springstudy.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value={AuditingEntityListener.class})// Auditing적용하기 위한 어노테이션
@MappedSuperclass// 공통 매핑 정보가 필요할 때 사용되는 어노테이션으로 부모클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@Getter@Setter
public abstract class BaseTimeEntity {

    // 등록일
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    // 수정일
    @LastModifiedDate
    private LocalDateTime modDate;
}
