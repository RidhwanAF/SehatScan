package com.healthy.sehatscan.data.remote.drink.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

class FavoriteDrink {
    data class GetFavoriteResponse(
        @field:SerializedName("data")
        val data: List<FavoriteItem>? = null,
        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null
    )

    data class Drink(
        @field:SerializedName("drink_name")
        val drinkName: String? = null,
        @field:SerializedName("description")
        val description: String? = null,
        @field:SerializedName("drink_id")
        val drinkId: Int? = null
    )

    data class FavoriteItem(
        @field:SerializedName("createdAt")
        val createdAt: String? = null,
        @field:SerializedName("favorite_id")
        val favoriteId: Int,
        @field:SerializedName("drink")
        val drink: Drink? = null,
        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    )
}