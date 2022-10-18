package com.chroneed.chroneedapp.data.medical

data class GetPrescriptionListResponse(
    val data: List<PrescriptionModel>,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class GetPrescriptionModelResponse(
    val data: PrescriptionModel,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class PrescriptionModel(
    val id: String?,
    val title: String?,
    val description: String?,
    val date: String?,
    val userId: String?,
    val userFullName: String?,
    val medicines: List<PrescriptionMedicineModel>?
)
data class UpdatePrescriptionModel(
    val id: String?,
    val date: String?,
    val description: String?,
    val medicines: List<PrescriptionMedicineModel?>?,
    val title: String?
)
data class NewPrescriptionModel(
    val date: String?,
    val description: String?,
    val medicines: List<PrescriptionMedicineModel?>?,
    val title: String?
)

data class PrescriptionMedicineModel(
    val dayOfWeek: List<Int>?,
    val note: String?,
    val administration: String?,
    val qty: String?,
    val dosageEvery: String?,
    val startUsingDate: String,
    val startUsingTime: String,
    val endUsingTimeName: String?,
    val endUsingTimeValue: String?,
    var imageName: String?,
    val medicineType: String?,
    val name: String?,
    val useEveryDuration: Int?,
    val endOccurrence: Int?
)

data class DeletePrescriptionResponse(
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)
