package com.chroneed.chroneedapp.data.medical

data class GetPractitionerListResponse(
    val count: Int?,
    val data: List<PractitionerModel>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class GetPractitionerResponse(
    val count: Int?,
    val data: PractitionerModel?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class PractitionerModel(
    val address: String?,
    val emergencyPhone: String?,
    val homeAddress: String?,
    val id: String?,
    val phone: String?,
    val specialty: String?,
    val type: Int?,
    val userId: String?
)