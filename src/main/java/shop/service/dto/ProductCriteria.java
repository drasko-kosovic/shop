package shop.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link shop.domain.Product} entity. This class is used
 * in {@link shop.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sku;

    private StringFilter name;

    private StringFilter description;

    private DoubleFilter unitPrice;

    private StringFilter imageUrl;

    private IntegerFilter active;

    private IntegerFilter unitsInStock;

    private InstantFilter dateCreated;

    private InstantFilter lastUpdated;

    private LongFilter productCategoryId;

    public ProductCriteria() {
    }

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sku = other.sku == null ? null : other.sku.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.unitPrice = other.unitPrice == null ? null : other.unitPrice.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.unitsInStock = other.unitsInStock == null ? null : other.unitsInStock.copy();
        this.dateCreated = other.dateCreated == null ? null : other.dateCreated.copy();
        this.lastUpdated = other.lastUpdated == null ? null : other.lastUpdated.copy();
        this.productCategoryId = other.productCategoryId == null ? null : other.productCategoryId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSku() {
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(DoubleFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public IntegerFilter getActive() {
        return active;
    }

    public void setActive(IntegerFilter active) {
        this.active = active;
    }

    public IntegerFilter getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(IntegerFilter unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public InstantFilter getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(InstantFilter lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LongFilter getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(LongFilter productCategoryId) {
        this.productCategoryId = productCategoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(active, that.active) &&
            Objects.equals(unitsInStock, that.unitsInStock) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(lastUpdated, that.lastUpdated) &&
            Objects.equals(productCategoryId, that.productCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sku,
        name,
        description,
        unitPrice,
        imageUrl,
        active,
        unitsInStock,
        dateCreated,
        lastUpdated,
        productCategoryId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sku != null ? "sku=" + sku + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (unitsInStock != null ? "unitsInStock=" + unitsInStock + ", " : "") +
                (dateCreated != null ? "dateCreated=" + dateCreated + ", " : "") +
                (lastUpdated != null ? "lastUpdated=" + lastUpdated + ", " : "") +
                (productCategoryId != null ? "productCategoryId=" + productCategoryId + ", " : "") +
            "}";
    }

}
