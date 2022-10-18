package com.chroneed.chroneedapp.data.medical

data class GetMedicalMedicineListResponse(
    val count: Int?,
    val data: List<MedicalMedicineModel>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class GetMedicalMedicineResponse(
    val count: Int?,
    val data: MedicalMedicineModel,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class MedicalMedicineModel(
    val id: String?,
    val name: String?,
    val images: String?,
    val administration: String?,
    val qty: String?,
    val dosageEvery: Int?,
    val startUsingDate: String?,
    val medicineId: String?,
    val userId: String?,
    val dayOfWeeks: List<Int>?,
    val endType: Int?,
    val endOn: String?,
    val endAfter: Int?,
)

data class GetUserMedicineResponse(
    val count: Int?,
    val data: List<UserMedicineModel>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class UserMedicineModel(
    val id: String?,
    val name: String?,
    val images: String?,
    val administration: String?,
    val qty: String?,
    val dosageEvery: Int?,
    val startUsingDate: String?,
    val medicineId: String?,
    val userId: String?,
    val dayOfWeeks: List<Int>?,
    val endType: Int?,
    val endOn: String?,
    val endAfter: Int?,
)

data class GetMedicineResponse(
    val count: Int?,
    val data: List<MedicineModel>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)
data class MedicineModel(
    val id: String?,
    val name: String?,
    val administration: String?,
    val qty: String?,
    val isConfirmed: Boolean?,
)
