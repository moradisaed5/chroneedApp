package com.chroneed.chroneedapp.data.medical

data class GetMedicalGoalResponse(
    val data: MedicalGoalModel,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class GetMedicalGoalListResponse(
    val data: List<MedicalGoalModel>,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class MedicalGoalModel(
    val id: String?,
    val maxRepeat: Int?,
    val repeatTime: Int?,
    val repeatType: Int?,
    val title: String?,
    val userFullName: String?,
    val userId: String?
)

data class SetGoalRequest(
    val relatedId: String?,
    val goalType: Int?
)

data class SetGoalResponse(
    val data: SetGoalModel?,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class SetGoalModel(
    val id: String?,
    val setGoal: Int?,
    val isGoalTaked: Boolean?,
    val dateTime: String?,
    val relatedId: String?,
    val goalType: Int?
)

data class GetGoalStaticsResponse(
    val data: List<GoalStaticsModel>?,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class GoalStaticsModel(
    val todayCount: Int,
    val weekCount: Int,
    val monthCount: Int,
    val yearCount: Int,
    val allCount: Int,
    val goalType: Int,
)