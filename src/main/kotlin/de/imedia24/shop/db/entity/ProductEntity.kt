package de.imedia24.shop.db.entity

import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "products")
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    @Column(name = "sku", nullable = false)
    val sku: String,
    
    @Column(name = "name", nullable = false)
    val name: String,
    
    @Column(name = "description")
    val description: String? = null,
    
    @Column(name = "price", nullable = false)
    val price: BigDecimal,
   
    @Column(name = "stock", nullable = false)
    val stock: Int,
    
    @UpdateTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: ZonedDateTime,
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: ZonedDateTime
) {
    // Add a no-args constructor for JPA
    constructor() : this(
        "", // sku
        "", // name
        null, // description
        BigDecimal.ZERO, // price
        0, // stock
        ZonedDateTime.now(), // createdAt
        ZonedDateTime.now() // updatedAt
    )
}