package br.com.TestLabs.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@Table(name = "\"product\"")
public class ProductEntity {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "value")
    private Double value;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
