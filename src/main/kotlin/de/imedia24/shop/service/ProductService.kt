package de.imedia24.shop.service

import de.imedia24.shop.db.entity.ProductEntity
import de.imedia24.shop.db.repository.ProductRepository
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductResponse.Companion.toProductResponse
import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductUpdateRequest
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findProductBySku(sku: String): ProductResponse? {
        val productEntity = productRepository.findBySku(sku)
        return productEntity?.toProductResponse()
    }
    
    fun findProductsBySkus(skus: List<String>): List<ProductResponse> {
        return productRepository.findAllById(skus)
            .map { it.toProductResponse() }
    }
    
    fun createProduct(productRequest: ProductRequest): ProductResponse? {
        // Check if product already exists
        val existingProduct = productRepository.findBySku(productRequest.sku)
        if (existingProduct != null) {
            return null
        }
        
        val now = ZonedDateTime.now()
        val productEntity = ProductEntity(
            sku = productRequest.sku,
            name = productRequest.name,
            description = productRequest.description,
            price = productRequest.price,
            stock = productRequest.stock ?: 0,
            createdAt = now,
            updatedAt = now
        )
        
        return productRepository.save(productEntity).toProductResponse()
    }
    
    fun updateProduct(sku: String, updateRequest: ProductUpdateRequest): ProductResponse? {
        val existingProduct = productRepository.findBySku(sku) ?: return null
        
        val updatedProduct = existingProduct.copy(
            name = updateRequest.name ?: existingProduct.name,
            description = updateRequest.description ?: existingProduct.description,
            price = updateRequest.price ?: existingProduct.price,
            stock = updateRequest.stock ?: existingProduct.stock,
            updatedAt = ZonedDateTime.now()
        )
        
        return productRepository.save(updatedProduct).toProductResponse()
    }
}