package com.healthy.sehatscan.data.remote.user.response

import com.google.gson.annotations.SerializedName
import com.healthy.sehatscan.data.remote.ResponseMetaData

class UserProfileData {
    data class UpdateProfileReqBody(
        @SerializedName("name")
        val name: String
    )

    data class UpdateProfileResponse(

        @field:SerializedName("data")
        val data: ProfileDataItem? = null,

        @field:SerializedName("meta")
        val meta: ResponseMetaData? = null
    )

    data class ProfileDataItem(

        @field:SerializedName("allergies")
        val allergies: List<AllergiesItem?>? = null,

        @field:SerializedName("createdAt")
        val createdAt: String? = null,

        @field:SerializedName("role")
        val role: Role? = null,

        @field:SerializedName("user_id")
        val userId: Int? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("history_diseases")
        val historyDiseases: List<HistoryDiseasesItem?>? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("updatedAt")
        val updatedAt: String? = null
    )

    data class AllergiesItem(

        @field:SerializedName("fruit")
        val fruit: Fruit? = null,

        @field:SerializedName("allergy_id")
        val allergyId: Int? = null
    )
}