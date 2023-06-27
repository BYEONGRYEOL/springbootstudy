package com.example.full.entity;

import jakarta.persistence.*;
import lombok.*;


@ToString
@Getter // getter 메소드를 생성하고, builder를 이용해 객체를 생성할 수 있게 처맇나다.
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="mytable3") // 클래스명과 테이블명이 다를때 매핑시켜주기위해 사용
@Entity // db의 테이블을 뜻함
public class JPAEntity {
    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //pk의 생성 전 설정
    private Long id;

    @Column(length =200, nullable =false)
    private String memoText;
}
