package com.chroneed.chroneedapp.data.social

data class SocialSearchUserResponse(
    val count: Int,
    val data: List<SocialSearchUserModel>?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int?
)

data class SocialSearchUserModel(
    val id: String?,
    val userName: String?,
    val firstName: String?,
    val lastName: String?,
    val biography: String?,
    val profileImageName: String?
)

data class SocialSearchPostResponse(
    val count: Int,
    val data: List<SocialPostModel>?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)

data class SocialSearchPostModel(
    val id: String,
    val title: String?,
    val description: String?,
    val imageName: String?,
    var likeCount: Int?,
    var isLiked: Boolean?,
    var readyToComment: Boolean?,
    var commentCount: Int?,
    var usersLike:List<String>?,
    val userPostedId: String?,
    val userFullName: String?
)