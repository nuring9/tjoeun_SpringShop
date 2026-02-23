package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})  // 이 엔티티에서 JPA Auditing 기능을 사용하겠다는 선언
// AuditingEntityListener가 엔티티의 저장/수정 시점을 감지함, 그리고 @CreatedDate, @LastModifiedDate 값을 자동으로 세팅해줌
@MappedSuperclass  // 이 클래스는 테이블로 생성되지 않음. 대신, 상속받는 엔티티가 이 필드를 컬럼으로 물려받음
@Getter
@Setter
public abstract class BaseTimeEntity {  // 직접 객체 생성 못함
    // Item 테이블에 regTime, updateTime 컬럼이 생성됨.
    @CreatedDate  // 엔티티가 처음 저장될 때 자동으로 시간 세팅, insert 시점에만 값 들어감
    @Column(updatable = false) // 한 번 저장되면 수정 불가, update 쿼리 때 이 컬럼은 변경되지 않음 (즉, 생성 시간 전용 필드)
    private LocalDateTime regTime;

    @LastModifiedDate // 엔티티가 수정될 때마다 자동으로 시간 갱신
    private LocalDateTime updateTime;   // insert 때도 들어가고, update 때도 자동 변경됨
}
