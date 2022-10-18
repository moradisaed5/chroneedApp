package com.chroneed.chroneedapp.data.medical

data class GetMedicalAlarmListResponse(
    val count: Int?,
    val data: List<MedicalAlarmModel>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class GetMedicalAlarmResponse(
    val count: Int?,
    val data: MedicalAlarmModel,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)
data class DeleteMedicalAlarmResponse(
    val count: Int?,
    val isSuccess: Boolean,
    val message: String?,
    val statusCode: Int?
)
data class GetAlarmNotifyListResponse(
    val count: Int?,
    val data: List<AlarmNotify>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)
data class AlarmNotify(
    val title: String,
    val description: String,
    val image: String?,
    val alarmTime: String?,
    val totalSecond: Int,
    val dateString: String?
)
data class MedicalAlarmModel(
    val dayOfWeeks: List<Int>?,
    val description: String?,
    val endDate: String?,
    val endType: Int?,
    var id: String?,
    var image: String?,
    val medicineId: String?,
    val repeatEvery: Int?,
    val repeatEveryType: Int?,
    val startDate: String?,
    val title: String?,
)