package br.com.TestLabs.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "\"order\"")
public class OrderEntity {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
