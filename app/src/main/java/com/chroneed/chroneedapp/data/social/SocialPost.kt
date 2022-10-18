package com.chroneed.chroneedapp.data.social


data class SocialPostListResponse(
    val count: Int,
    val data: List<SocialPostModel>?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)
data class SocialPostResponse(
    val count: Int?,
    val data: SocialPostModel?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)
data class SocialPostModel(
    val id: String,
    val title: String?,
    val description: String?,
    val imageName: String?,
    var likeCount: Int?,
    var isLiked: Boolean?,
    var readyToComment: Boolean=false,
    var commentCount: Int,
    val userPostedId: String?,
    val userFullName: String?
)
data class NewSocialPostModel(
    val title: String,
    val description: String,
    val imageName: String?
)
