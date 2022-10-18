package com.chroneed.chroneedapp.data.medical

data class GetMedicineListModelResponse(
    val count: Int?,
    val data: List<MedicineModels>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class SearchMedicineListModelV2Response(
    val count: Int?,
    val data: List<String>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class MedicineModels(
    val id: String?,
    val name: String?,
    val administration: String?,
    val qty: String?,
    val isConfirmed: Boolean?
)