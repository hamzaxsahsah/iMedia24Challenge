package de.imedia24.shop.domain.product

import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class ProductRequest(
    @field:NotBlank(message = "SKU is required")
    val sku: String,
    
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    val description: String? = null,
    
    @field:NotNull(message = "Price is required")
    @field:Positive(message = "Price must be positive")
    val price: BigDecimal,
    
    @field:Positive(message = "Stock must be positive")
    val stock: Int? = 0
)