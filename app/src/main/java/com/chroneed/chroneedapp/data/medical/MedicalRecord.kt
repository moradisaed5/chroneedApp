package com.chroneed.chroneedapp.data.medical

data class GetMedicalRecordListResponse(
    val count: Int?,
    val data: List<MedicalRecordModel>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class GetMedicalRecordResponse(
    val count: Int?,
    val data: MedicalRecordModel?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class MedicalRecordModel(
    val id: String?,
    val subject: String?,
    val startDate: String?,
    val endDate: String?,
    val isTreatmentIsOver: Boolean?,
    val description: String?,
    val images: List<String>?,
    val fullName: String?,
    val medicalRecordTypeId: String?,
    val medicalRecordStageId: String?,
    val strMedicalRecordType: String,
    val strMedicalRecordStage: String,
    val userId: String?
)

data class NewMedicalRecordRequest(
    val subject: String?,
    val startDate: String?,
    val endDate: String?,
    val isTreatmentIsOver: Boolean?,
    val description: String?,
    val images: List<String>?,
    val medicalRecordTypeId: String?,
    val medicalRecordStageId: String?,
)

data class UpdateMedicalRecordRequest(
    val id: String,
    val subject: String?,
    val startDate: String?,
    val endDate: String?,
    val isTreatmentIsOver: Boolean?,
    val description: String?,
    val images: List<String>?,
    val medicalRecordTypeId: String?,
    val medicalRecordStageId: String?,
)

data class GetMedicalRecordStageListResponse(
    val count: Int?,
    val data: List<MedicalRecordStage>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class MedicalRecordStage(
    val id: String?,
    val name: String?,
    val code: String?
)

data class GetMedicalRecordTypeListResponse(
    val count: Int?,
    val data: List<MedicalRecordType>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class MedicalRecordType(
    val id: String?,
    val name: String?,
    val code: String?
)
data class DeleteMedicalRecordResponse(
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

