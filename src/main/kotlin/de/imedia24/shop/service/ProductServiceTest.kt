package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductUpdateRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.ZonedDateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
class ProductServiceTest {

    private lateinit var productService: ProductService
    private lateinit var productRepository: ProductRepository

    @BeforeEach
    fun setUp() {
        productRepository = mock(ProductRepository::class.java)
        productService = ProductService(productRepository)
    }

    @Test
    fun `findProductBySku should return product response when product exists`() {
        // Given
        val sku = "123"
        val productEntity = ProductEntity(
            sku = sku,
            name = "Test Product",
            description = "Description",
            price = BigDecimal("10.99"),
            stock = 5,
            createdAt = ZonedDateTime.now(),
            updatedAt = ZonedDateTime.now()
        )
        whenever(productRepository.findBySku(sku)).thenReturn(productEntity)

        // When
        val result = productService.findProductBySku(sku)

        // Then
        assertEquals(sku, result?.sku)
        assertEquals("Test Product", result?.name)
    }

    @Test
    fun `findProductBySku should return null when product does not exist`() {
        // Given
        val sku = "nonexistent"
        whenever(productRepository.findBySku(sku)).thenReturn(null)

        // When
        val result = productService.findProductBySku(sku)

        // Then
        assertNull(result)
    }

    @Test
    fun `findProductsBySkus should return list of products`() {
        // Given
        val skus = listOf("123", "456")
        val productEntities = listOf(
            ProductEntity(
                sku = "123",
                name = "Product 1",
                description = "Description 1",
                price = BigDecimal("10.99"),
                stock = 5,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            ),
            ProductEntity(
                sku = "456",
                name = "Product 2",
                description = "Description 2",
                price = BigDecimal("20.99"),
                stock = 10,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now()
            )
        )
        whenever(productRepository.findAllById(skus)).thenReturn(productEntities)

        // When
        val result = productService.findProductsBySkus(skus)

        // Then
        assertEquals(2, result.size)
        assertEquals("123", result[0].sku)
        assertEquals("456", result[1].sku)
    }

    @Test
    fun `updateProduct should return updated product when product exists`() {
        // Given
        val sku = "123"
        val updateRequest = ProductUpdateRequest(
            name = "Updated Product",
            price = BigDecimal("15.99")
        )
        
        val existingProduct = ProductEntity(
            sku = sku,
            name = "Original Product",
            description = "Original Description",
            price = BigDecimal("10.99"),
            stock = 5,
            createdAt = ZonedDateTime.now(),
            updatedAt = ZonedDateTime.now()
        )
        
        val updatedProduct = existingProduct.copy(
            name = "Updated Product",
            price = BigDecimal("15.99")
        )
        
        whenever(productRepository.findBySku(sku)).thenReturn(existingProduct)
        whenever(productRepository.save(any())).thenReturn(updatedProduct)

        // When
        val result = productService.updateProduct(sku, updateRequest)

        // Then
        assertEquals("Updated Product", result?.name)
        assertEquals(BigDecimal("15.99"), result?.price)
    }
}