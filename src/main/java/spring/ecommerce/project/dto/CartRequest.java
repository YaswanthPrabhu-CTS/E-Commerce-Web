package spring.ecommerce.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

//record automatically gives constructor, getters, equals, hashCode, toString
public record CartRequest(
        @NotNull(message = "Product ID is required")
        Long productId,
        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}

/*
public class CartRequest {
        private final Long productId;
        private final Integer quantity;
        public CartRequest(Long productId, Integer quantity) {
                this.productId = productId;
                this.quantity = quantity;
        }
        public Long getProductId() {
               return productId;
        }
        public Integer getQuantity() {
               return quantity;
        }
}
 */