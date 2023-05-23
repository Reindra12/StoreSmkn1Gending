package com.example.storesmkn1gending.model

import java.io.Serializable


data class ResponseProduct(
    val responseProduct: List<ResponseProductItem>
)

data class CategoriesItem(
    val name: String,
    val id: Int
)

data class ResponseProductItem(

    val imageName: String,
    val createdAt: String,
    val userId: Int,
//	val categories: List<CategoriesItem>,
    val imageUrl: String,

    val name: String,

    val basePrice: Int,

    val description: String,

    val location: String,
    val id: Int,
    val status: String,
    val updatedAt: String
) : Serializable
