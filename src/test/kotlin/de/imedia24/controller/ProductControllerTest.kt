package de.imedia24.shop.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var productService: ProductService

    @Test
    fun `should return products by SKUs`() {
        // Given
        val skus = "123,456"
        val productResponses = listOf(
            ProductResponse("123", "Product 1", "Description 1", BigDecimal("10.99"), 5),
            ProductResponse("456", "Product 2", "Description 2", BigDecimal("20.99"), 10)
        )
        `when`(productService.findProductsBySkus(listOf("123", "456"))).thenReturn(productResponses)

        // When & Then
        mockMvc.perform(get("/products")
                .param("skus", skus)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].sku").value("123"))
            .andExpect(jsonPath("$[0].name").value("Product 1"))
            .andExpect(jsonPath("$[1].sku").value("456"))
            .andExpect(jsonPath("$[1].name").value("Product 2"))
    }

    @Test
    fun `should update product partially`() {
        // Given
        val sku = "123"
        val updateRequest = ProductUpdateRequest(
            name = "Updated Product",
            price = BigDecimal("15.99")
        )
        val updatedProduct = ProductResponse(
            sku = "123",
            name = "Updated Product",
            description = "Description",
            price = BigDecimal("15.99"),
            stock = 5
        )
        
        `when`(productService.updateProduct(sku, updateRequest)).thenReturn(updatedProduct)

        // When & Then
        mockMvc.perform(patch("/product/$sku")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.sku").value("123"))
            .andExpect(jsonPath("$.name").value("Updated Product"))
            .andExpect(jsonPath("$.price").value(15.99))
    }
    
    @Test
    fun `should return 404 when updating non-existent product`() {
        // Given
        val sku = "nonexistent"
        val updateRequest = ProductUpdateRequest(
            name = "Updated Product"
        )
        
        `when`(productService.updateProduct(sku, updateRequest)).thenReturn(null)

        // When & Then
        mockMvc.perform(patch("/product/$sku")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isNotFound)
    }
}