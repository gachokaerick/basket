package com.gachokaerick.eshop.basket.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gachokaerick.eshop.basket.domain.BasketItem} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class BasketItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private String productName;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal oldUnitPrice;

    @NotNull
    private Integer quantity;

    @NotNull
    private String pictureUrl;

    @NotNull
    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getOldUnitPrice() {
        return oldUnitPrice;
    }

    public void setOldUnitPrice(BigDecimal oldUnitPrice) {
        this.oldUnitPrice = oldUnitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasketItemDTO)) {
            return false;
        }

        BasketItemDTO basketItemDTO = (BasketItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, basketItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BasketItemDTO{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", productName='" + getProductName() + "'" +
            ", unitPrice=" + getUnitPrice() +
            ", oldUnitPrice=" + getOldUnitPrice() +
            ", quantity=" + getQuantity() +
            ", pictureUrl='" + getPictureUrl() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
