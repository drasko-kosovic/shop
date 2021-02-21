package shop.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.domain.Product;
import shop.repository.ProductRepository;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {
  private final Logger log = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product save(Product product) {
    log.debug("Request to save Product : {}", product);
    return productRepository.save(product);
  }

  /**
   * Get all the products.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<Product> findAll(Pageable pageable) {
    log.debug("Request to get all Products");
    return productRepository.findAll(pageable);
  }

  /**
   * Get one product by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<Product> findOne(Long id) {
    log.debug("Request to get Product : {}", id);
    return productRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<Product> findByProductCategoryId(Long ProductCategoryId) {
    log.debug("Request to get Product : {}", ProductCategoryId);
    return productRepository.findByProductCategoryId(ProductCategoryId);
  }

    @Transactional(readOnly = true)
    public List<Product> findByProductName(String name) {
        log.debug("Request to get Product : {}", name);
        return productRepository.findByNameContaining(name);
    }

  /**
   * Delete the product by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Product : {}", id);
    productRepository.deleteById(id);
  }
}
