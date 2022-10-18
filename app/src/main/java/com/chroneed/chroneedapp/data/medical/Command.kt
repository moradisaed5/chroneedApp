package com.chroneed.chroneedapp.data.medical

/*
        Justlabel=0,
        Call=1,
        Message=2,
        Medicine=3,
        Habit=4,
        Goal=5
        * */

data class DeleteCommandResponse(
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class SendVoiceResponseDto(
    val data: List<String>? = null,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class GetVoiceResponseDto(
    val data: List<String>? = null,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class TrainResponseDto(
    val data: GetRecognizeCommandModel?,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String
)

data class DeleteVoiceResponseDto(
    val data: Boolean,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String
)

data class UserCommandsResponse(
    val count: Int?,
    val data: List<GetCommandModel>?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class AddUserCommandResponse(
    val count: Int?,
    val data: NewCommandModel?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
)

data class NewCommandModel(
    val callCommand: CallCommand?,
    val commanType: Int,
    val commandName: String,
    val label: String,
    val messageCommand: MessageCommand?,
    val relatedGoal: RelatedGoal?,
    val relatedHabit: RelatedHabit?,
    val relatedMedicine: RelatedMedicine?
) {
    data class CallCommand(
        val phoneNumber: String?
    )

    data class MessageCommand(
        val phoneNumber: String?,
        val textMessage: String?
    )

    data class RelatedGoal(
        val goalId: String?
    )

    data class RelatedHabit(
        val habitId: String?
    )

    data class RelatedMedicine(
        val medicineId: String?
    )
}


data class GetCommandModel(
    val id: String,
    val callCommand: CallCommand?,
    val commanType: Int,
    val commandName: String,
    val label: String,
    val messageCommand: MessageCommand?,
    val relatedGoal: RelatedGoal?,
    val relatedHabit: RelatedHabit?,
    val relatedMedicine: RelatedMedicine?
) {
    data class CallCommand(
        val phoneNumber: String?
    )

    data class MessageCommand(
        val phoneNumber: String?,
        val textMessage: String?
    )

    data class RelatedGoal(
        val goalId: String?
    )

    data class RelatedHabit(
        val habitId: String?
    )

    data class RelatedMedicine(
        val medicineId: String?
    )
}

data class GetRecognizeCommandModel(
    val command: String,
    val callCommand: CallCommand?,
    val type: Int,
    val messageCommand: MessageCommand?,
    val relatedGoal: RelatedGoal?,
    val relatedHabit: RelatedHabit?,
    val relatedMedicine: RelatedMedicine?
) {
    data class CallCommand(
        val phoneNumber: String?
    )

    data class MessageCommand(
        val phoneNumber: String?,
        val textMessage: String?
    )

    data class RelatedGoal(
        val goalId: String?
    )

    data class RelatedHabit(
        val habitId: String?
    )

    data class RelatedMedicine(
        val medicineId: String?
    )
}