package br.com.TestLabs.repositories;

import br.com.TestLabs.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("select o from OrderEntity o where o.id = :id and o.user.id = :userId")
    Optional<OrderEntity> findOrderByIdAndUserId(@Param("id") Long id,
                                                 @Param("userId") Long userId);
}
