package com.chroneed.chroneedapp.data.social

data class SocialCommentListResponse(
    val count: Int,
    val data: List<SocialCommentModel>?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)
data class LikeResponse(
    val isSuccess: Boolean=true
)
data class SocialCommentResponse(
    val count: Int,
    val data: SocialCommentModel?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)
data class SocialCommentModel(
    val id: String?,
    val fullName: String?,
    val userId: String?,
    val postId: String?,
    val profileImage: String?,
    val comment: String?
)
data class NewSocialCommentModel(
    val postId: String,
    val comment: String
)