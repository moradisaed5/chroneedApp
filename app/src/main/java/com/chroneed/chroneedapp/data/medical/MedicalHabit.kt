package com.chroneed.chroneedapp.data.medical

data class GetMedicalHabitListResponse(
    val count: Int?,
    val data: List<MedicalHabitModel>,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class GetMedicalHabitResponse(
    val count: Int?,
    val data: MedicalHabitModel?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class MedicalHabitModel(
    val daysOfWeek: MutableList<Int>?,
    val description: String?,
    val endDate: String?,
    val strEndDate: String?,
    val id: String?,
    val image: String?,
    val remindTime: String?,
    val strRemindTime: String?,
    val startDate: String?,
    val strStartDate: String?,
    val title: String?,
    val userId: String?
)

data class NewMedicalHabitModel(
    val daysOfWeek: MutableList<Int>?,
    val description: String?,
    val endDate: String?,
    var image: String?,
    val remindTime: String?,
    val startDate: String?,
    val title: String?
)
data class UpdateMedicalHabitModel(
    val daysOfWeek: MutableList<Int>?,
    val description: String?,
    val id: String?,
    val endDate: String?,
    var image: String?,
    val strRemindTime: String?,
    val startDate: String?,
    val title: String?
)

enum class enumDayOfWeek(val value: Int) {
    Su(0),
    Mo(1),
    Tu(2),
    We(3),
    Th(4),
    Fr(5),
    Sa(6);
    companion object {
        fun fromInt(value: Int) = enumDayOfWeek.values().first { it.value == value }
    }
}

data class DeleteMedicalHabitResponse(
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)