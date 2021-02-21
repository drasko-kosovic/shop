package shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import shop.domain.Product;

/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByProductCategoryId(@RequestParam("ProductCategoryId") Long ProductCategoryId);
    List<Product> findByNameContaining(@RequestParam("name") String name);


}
