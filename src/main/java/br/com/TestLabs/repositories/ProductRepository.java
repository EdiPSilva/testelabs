package br.com.TestLabs.repositories;

import br.com.TestLabs.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query("select p from ProductEntity p where p.id = :id and p.order.id = :orderId")
    Optional<ProductEntity> findProductByIdAndOrderId(@Param("id") Long id,
                                                      @Param("orderId") Long orderId);

    @Query("select SUM(p.value) from ProductEntity p where p.order.id = :orderId")
    Double totalAmountByOrder(@Param("orderId") Long orderId);
}
