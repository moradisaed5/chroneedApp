package com.chroneed.chroneedapp.data.medical

data class CarePlanModel(
    val id: String,
    val userId: String,
    val doctorName: String,
    val planDate: String,
    val planDescription: String
)

data class GetCarePlanResponse(
    val count: Int?,
    val data: CarePlanModel?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class GetCarePlanListResponse(
    val count: Int?,
    val data: List<CarePlanModel>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)
data class DeleteCarePlanResponse(
    val count: Int?,
    val isSuccess: Boolean,
    val message: String?,
    val statusCode: Int?
)

data class NewCarePlanRequest(
    val doctorName: String?,
    val planDate: String?,
    val planDescription: String?
)

data class UpdateCarePlanRequest(
    val id: String?,
    val doctorName: String?,
    val planDate: String?,
    val planDescription: String?
)

