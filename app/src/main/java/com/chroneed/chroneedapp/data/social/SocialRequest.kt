package com.chroneed.chroneedapp.data.social

data class SocialRequestListResponse(
    val count: Int,
    val data: List<SocialRequestModel>?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)
data class SocialRequestResponse(
    val count: Int,
    val data: SocialRequestModel?,
    val isSuccess: Boolean,
    val message: String,
    val statusCode: Int
)
data class SocialRequestModel(
    val id: String?,
    val userSendedRequestId: String?,
    val userRecivedRequestedId: String?,
    val createdDateTime: String?,
    val fullName: String?,
    val requestDate: String?,
    val isShowRequest: Boolean?,
    val profilaImageName: String?,
    val requestStatus: Int?,
)