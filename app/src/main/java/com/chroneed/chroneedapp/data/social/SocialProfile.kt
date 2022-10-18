package com.chroneed.chroneedapp.data.social

data class SocialProfileResponse(
    val count: Int,
    val data: SocialProfileModel?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)

data class SocialProfileModel(
    val id: String?,
    val userId: String?,
    val isPrivate: Boolean?,
    val postCount: Int?,
    val fllowingCount: Int?,
    val fllowerCount: Int?,
    val bio: String?,
    val imageProfileName: String?,
    val userFirstName: String?,
    val userLastName: String?,
    val followStatus: Int?,
    val userFullName: String?
)

data class NewSocialProfileModel(
    var isPrivate: Boolean,
    val bio: String,
    val imageProfileName: String?,
    val firstName: String,
    val lastName: String
)