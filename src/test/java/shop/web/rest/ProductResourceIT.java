package shop.web.rest;

import shop.ShopApp;
import shop.domain.Product;
import shop.domain.ProductCategory;
import shop.repository.ProductRepository;
import shop.service.ProductService;
import shop.service.dto.ProductCriteria;
import shop.service.ProductQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_PRICE = 1D;
    private static final Double UPDATED_UNIT_PRICE = 2D;
    private static final Double SMALLER_UNIT_PRICE = 1D - 1D;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACTIVE = 1;
    private static final Integer UPDATED_ACTIVE = 2;
    private static final Integer SMALLER_ACTIVE = 1 - 1;

    private static final Integer DEFAULT_UNITS_IN_STOCK = 1;
    private static final Integer UPDATED_UNITS_IN_STOCK = 2;
    private static final Integer SMALLER_UNITS_IN_STOCK = 1 - 1;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .sku(DEFAULT_SKU)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .active(DEFAULT_ACTIVE)
            .unitsInStock(DEFAULT_UNITS_IN_STOCK)
            .dateCreated(DEFAULT_DATE_CREATED)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .imageUrl(UPDATED_IMAGE_URL)
            .active(UPDATED_ACTIVE)
            .unitsInStock(UPDATED_UNITS_IN_STOCK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testProduct.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProduct.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testProduct.getUnitsInStock()).isEqualTo(DEFAULT_UNITS_IN_STOCK);
        assertThat(testProduct.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testProduct.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].unitsInStock").value(hasItem(DEFAULT_UNITS_IN_STOCK)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.unitsInStock").value(DEFAULT_UNITS_IN_STOCK))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductsBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku equals to DEFAULT_SKU
        defaultProductShouldBeFound("sku.equals=" + DEFAULT_SKU);

        // Get all the productList where sku equals to UPDATED_SKU
        defaultProductShouldNotBeFound("sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllProductsBySkuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku not equals to DEFAULT_SKU
        defaultProductShouldNotBeFound("sku.notEquals=" + DEFAULT_SKU);

        // Get all the productList where sku not equals to UPDATED_SKU
        defaultProductShouldBeFound("sku.notEquals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllProductsBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku in DEFAULT_SKU or UPDATED_SKU
        defaultProductShouldBeFound("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU);

        // Get all the productList where sku equals to UPDATED_SKU
        defaultProductShouldNotBeFound("sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllProductsBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku is not null
        defaultProductShouldBeFound("sku.specified=true");

        // Get all the productList where sku is null
        defaultProductShouldNotBeFound("sku.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsBySkuContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku contains DEFAULT_SKU
        defaultProductShouldBeFound("sku.contains=" + DEFAULT_SKU);

        // Get all the productList where sku contains UPDATED_SKU
        defaultProductShouldNotBeFound("sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    public void getAllProductsBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where sku does not contain DEFAULT_SKU
        defaultProductShouldNotBeFound("sku.doesNotContain=" + DEFAULT_SKU);

        // Get all the productList where sku does not contain UPDATED_SKU
        defaultProductShouldBeFound("sku.doesNotContain=" + UPDATED_SKU);
    }


    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name not equals to DEFAULT_NAME
        defaultProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productList where name not equals to UPDATED_NAME
        defaultProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description not equals to DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description not equals to UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice not equals to DEFAULT_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.notEquals=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice not equals to UPDATED_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.notEquals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the productList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is not null
        defaultProductShouldBeFound("unitPrice.specified=true");

        // Get all the productList where unitPrice is null
        defaultProductShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is greater than or equal to DEFAULT_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is greater than or equal to UPDATED_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is less than or equal to DEFAULT_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is less than or equal to SMALLER_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is less than DEFAULT_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is less than UPDATED_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.lessThan=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitPrice is greater than DEFAULT_UNIT_PRICE
        defaultProductShouldNotBeFound("unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);

        // Get all the productList where unitPrice is greater than SMALLER_UNIT_PRICE
        defaultProductShouldBeFound("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE);
    }


    @Test
    @Transactional
    public void getAllProductsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultProductShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the productList where imageUrl equals to UPDATED_IMAGE_URL
        defaultProductShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultProductShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the productList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultProductShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultProductShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the productList where imageUrl equals to UPDATED_IMAGE_URL
        defaultProductShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl is not null
        defaultProductShouldBeFound("imageUrl.specified=true");

        // Get all the productList where imageUrl is null
        defaultProductShouldNotBeFound("imageUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl contains DEFAULT_IMAGE_URL
        defaultProductShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the productList where imageUrl contains UPDATED_IMAGE_URL
        defaultProductShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllProductsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultProductShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the productList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultProductShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }


    @Test
    @Transactional
    public void getAllProductsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active equals to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active not equals to DEFAULT_ACTIVE
        defaultProductShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the productList where active not equals to UPDATED_ACTIVE
        defaultProductShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultProductShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is not null
        defaultProductShouldBeFound("active.specified=true");

        // Get all the productList where active is null
        defaultProductShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is greater than or equal to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.greaterThanOrEqual=" + DEFAULT_ACTIVE);

        // Get all the productList where active is greater than or equal to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.greaterThanOrEqual=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is less than or equal to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.lessThanOrEqual=" + DEFAULT_ACTIVE);

        // Get all the productList where active is less than or equal to SMALLER_ACTIVE
        defaultProductShouldNotBeFound("active.lessThanOrEqual=" + SMALLER_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is less than DEFAULT_ACTIVE
        defaultProductShouldNotBeFound("active.lessThan=" + DEFAULT_ACTIVE);

        // Get all the productList where active is less than UPDATED_ACTIVE
        defaultProductShouldBeFound("active.lessThan=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is greater than DEFAULT_ACTIVE
        defaultProductShouldNotBeFound("active.greaterThan=" + DEFAULT_ACTIVE);

        // Get all the productList where active is greater than SMALLER_ACTIVE
        defaultProductShouldBeFound("active.greaterThan=" + SMALLER_ACTIVE);
    }


    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock equals to DEFAULT_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.equals=" + DEFAULT_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock equals to UPDATED_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.equals=" + UPDATED_UNITS_IN_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock not equals to DEFAULT_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.notEquals=" + DEFAULT_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock not equals to UPDATED_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.notEquals=" + UPDATED_UNITS_IN_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock in DEFAULT_UNITS_IN_STOCK or UPDATED_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.in=" + DEFAULT_UNITS_IN_STOCK + "," + UPDATED_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock equals to UPDATED_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.in=" + UPDATED_UNITS_IN_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock is not null
        defaultProductShouldBeFound("unitsInStock.specified=true");

        // Get all the productList where unitsInStock is null
        defaultProductShouldNotBeFound("unitsInStock.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock is greater than or equal to DEFAULT_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.greaterThanOrEqual=" + DEFAULT_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock is greater than or equal to UPDATED_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.greaterThanOrEqual=" + UPDATED_UNITS_IN_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock is less than or equal to DEFAULT_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.lessThanOrEqual=" + DEFAULT_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock is less than or equal to SMALLER_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.lessThanOrEqual=" + SMALLER_UNITS_IN_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock is less than DEFAULT_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.lessThan=" + DEFAULT_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock is less than UPDATED_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.lessThan=" + UPDATED_UNITS_IN_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByUnitsInStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where unitsInStock is greater than DEFAULT_UNITS_IN_STOCK
        defaultProductShouldNotBeFound("unitsInStock.greaterThan=" + DEFAULT_UNITS_IN_STOCK);

        // Get all the productList where unitsInStock is greater than SMALLER_UNITS_IN_STOCK
        defaultProductShouldBeFound("unitsInStock.greaterThan=" + SMALLER_UNITS_IN_STOCK);
    }


    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated equals to DEFAULT_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.equals=" + DEFAULT_DATE_CREATED);

        // Get all the productList where dateCreated equals to UPDATED_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated not equals to DEFAULT_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.notEquals=" + DEFAULT_DATE_CREATED);

        // Get all the productList where dateCreated not equals to UPDATED_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.notEquals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated in DEFAULT_DATE_CREATED or UPDATED_DATE_CREATED
        defaultProductShouldBeFound("dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED);

        // Get all the productList where dateCreated equals to UPDATED_DATE_CREATED
        defaultProductShouldNotBeFound("dateCreated.in=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    public void getAllProductsByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where dateCreated is not null
        defaultProductShouldBeFound("dateCreated.specified=true");

        // Get all the productList where dateCreated is null
        defaultProductShouldNotBeFound("dateCreated.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastUpdated equals to DEFAULT_LAST_UPDATED
        defaultProductShouldBeFound("lastUpdated.equals=" + DEFAULT_LAST_UPDATED);

        // Get all the productList where lastUpdated equals to UPDATED_LAST_UPDATED
        defaultProductShouldNotBeFound("lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProductsByLastUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastUpdated not equals to DEFAULT_LAST_UPDATED
        defaultProductShouldNotBeFound("lastUpdated.notEquals=" + DEFAULT_LAST_UPDATED);

        // Get all the productList where lastUpdated not equals to UPDATED_LAST_UPDATED
        defaultProductShouldBeFound("lastUpdated.notEquals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProductsByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastUpdated in DEFAULT_LAST_UPDATED or UPDATED_LAST_UPDATED
        defaultProductShouldBeFound("lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED);

        // Get all the productList where lastUpdated equals to UPDATED_LAST_UPDATED
        defaultProductShouldNotBeFound("lastUpdated.in=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void getAllProductsByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where lastUpdated is not null
        defaultProductShouldBeFound("lastUpdated.specified=true");

        // Get all the productList where lastUpdated is null
        defaultProductShouldNotBeFound("lastUpdated.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByProductCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        ProductCategory productCategory = ProductCategoryResourceIT.createEntity(em);
        em.persist(productCategory);
        em.flush();
        product.setProductCategory(productCategory);
        productRepository.saveAndFlush(product);
        Long productCategoryId = productCategory.getId();

        // Get all the productList where productCategory equals to productCategoryId
        defaultProductShouldBeFound("productCategoryId.equals=" + productCategoryId);

        // Get all the productList where productCategory equals to productCategoryId + 1
        defaultProductShouldNotBeFound("productCategoryId.equals=" + (productCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].unitsInStock").value(hasItem(DEFAULT_UNITS_IN_STOCK)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));

        // Check, that the count call also returns 1
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productService.save(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .sku(UPDATED_SKU)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .imageUrl(UPDATED_IMAGE_URL)
            .active(UPDATED_ACTIVE)
            .unitsInStock(UPDATED_UNITS_IN_STOCK)
            .dateCreated(UPDATED_DATE_CREATED)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProduct)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testProduct.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProduct.getUnitsInStock()).isEqualTo(UPDATED_UNITS_IN_STOCK);
        assertThat(testProduct.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testProduct.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productService.save(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
