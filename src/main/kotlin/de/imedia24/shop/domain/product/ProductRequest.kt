package de.imedia24.shop.domain.product

import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
// Add other imports as needed for your domain classes and repositories
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

data class ProductUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val price: BigDecimal? = null,
    val stock: Int? = null
)