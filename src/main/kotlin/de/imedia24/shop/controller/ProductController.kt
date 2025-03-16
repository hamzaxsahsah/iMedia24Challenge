package de.imedia24.shop.controller

import de.imedia24.shop.domain.product.ProductRequest
import de.imedia24.shop.domain.product.ProductResponse
import de.imedia24.shop.domain.product.ProductUpdateRequest
import de.imedia24.shop.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@Tag(name = "Products", description = "Product management APIs")
class ProductController(private val productService: ProductService) {

    private val logger = LoggerFactory.getLogger(ProductController::class.java)!!

    @GetMapping("/product/{sku}", produces = ["application/json;charset=utf-8"])
    @Operation(
        summary = "Find product by SKU",
        responses = [
            ApiResponse(responseCode = "200", description = "Product found"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    fun findProductBySku(
        @PathVariable("sku") sku: String
    ): ResponseEntity<ProductResponse> {
        logger.info("Request for product with SKU: $sku")

        val product = productService.findProductBySku(sku)
        return if(product == null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(product)
        }
    }

    @GetMapping("/products", produces = ["application/json;charset=utf-8"])
    @Operation(
        summary = "Find products by SKUs",
        responses = [
            ApiResponse(responseCode = "200", description = "List of products")
        ]
    )
    fun findProductsBySkus(
        @RequestParam("skus") skus: String
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Request for products with SKUs: $skus")
        
        val skuList = skus.split(",").map { it.trim() }
        val products = productService.findProductsBySkus(skuList)
        
        return ResponseEntity.ok(products)
    }

    @PostMapping("/product", produces = ["application/json;charset=utf-8"])
    @Operation(
        summary = "Create a new product",
        responses = [
            ApiResponse(responseCode = "201", description = "Product created"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "409", description = "Product already exists")
        ]
    )
    fun createProduct(
        @Valid @RequestBody productRequest: ProductRequest
    ): ResponseEntity<ProductResponse> {
        logger.info("Request to create product with SKU: ${productRequest.sku}")
        
        val createdProduct = productService.createProduct(productRequest)
        
        return if (createdProduct != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
        } else {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PatchMapping("/product/{sku}", produces = ["application/json;charset=utf-8"])
    @Operation(
        summary = "Update a product partially",
        responses = [
            ApiResponse(responseCode = "200", description = "Product updated"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    fun updateProduct(
        @PathVariable("sku") sku: String,
        @Valid @RequestBody updateRequest: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        logger.info("Request to update product with SKU: $sku")
        
        val updatedProduct = productService.updateProduct(sku, updateRequest)
        
        return if (updatedProduct != null) {
            ResponseEntity.ok(updatedProduct)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}