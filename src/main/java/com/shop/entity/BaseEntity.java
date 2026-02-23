package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class}) // EntityListeners : 이 엔티티에 이벤트 리스너를 붙이겠다.
// 엔티티 저장/수정 이벤트 발생 시 AuditingEntityListener가 개입, @CreatedBy, @LastModifiedBy 자동 세팅
@MappedSuperclass // 공통 필드를 자식 엔티티에 물려주기 위한 부모 클래스
// 이 클래스는 테이블로 생성X, 상속받는 엔티티의 테이블에 컬럼 포함
@Getter

public abstract class BaseEntity extends BaseTimeEntity {
    // BaseTimeEntity(시간 필드) 상속 거기에 작성자(createdBy), 수정자(modifiedBy) 추가
    // BaseTimeEntity -> 시간 관리 / BaseEntity -> 시간 + 작성자 관리


    @CreatedBy // 엔티티 처음 저장할 때 자동 입력
    @Column(updatable = false) // 수정 시 변경 안 됨
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}
