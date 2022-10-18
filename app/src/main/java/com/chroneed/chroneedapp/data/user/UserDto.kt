package com.chroneed.chroneedapp.data.user

import com.google.gson.annotations.SerializedName


//--------------------------    Dto     ----------------------------------
data class LoginDto(
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("password")
    val password: String?
)

data class RegisterDto(
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?
)

data class UpdateUserProfileDto(
    @SerializedName("biography")
    val biography: String?,
    @SerializedName("fatherName")
    val fatherName: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("profileImageName")
    val profileImageName: String?,
    @SerializedName("userName")
    val userName: String?
)

//--------------------------    Response     ----------------------------------
data class LoginResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("isSuccess")
    val isSuccess: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("statusCode")
    val statusCode: Int?
) {
    data class Data(
        @SerializedName("accessFailedCount")
        val accessFailedCount: Int?,
        @SerializedName("activationCode")
        val activationCode: String?,
        @SerializedName("biography")
        val biography: String?,
        @SerializedName("birthDate")
        val birthDate: String?,
        @SerializedName("concurrencyStamp")
        val concurrencyStamp: String?,
        @SerializedName("createdDateTime")
        val createdDateTime: String?,
        @SerializedName("cridit")
        val cridit: Int?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("emailConfirmed")
        val emailConfirmed: Boolean?,
        @SerializedName("fatherName")
        val fatherName: String?,
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("forgetPasswordCode")
        val forgetPasswordCode: String?,
        @SerializedName("forgetPasswordExpireDate")
        val forgetPasswordExpireDate: String?,
        @SerializedName("gender")
        val gender: Int?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlock")
        val isBlock: Boolean?,
        @SerializedName("isDeleted")
        val isDeleted: Boolean?,
        @SerializedName("isManager")
        val isManager: Boolean?,
        @SerializedName("isVerified")
        val isVerified: Boolean?,
        @SerializedName("jwtToken")
        val jwtToken: String?,
        @SerializedName("lastLoginDate")
        val lastLoginDate: String?,
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("lastRequest")
        val lastRequest: String?,
        @SerializedName("lastRequestName")
        val lastRequestName: String?,
        @SerializedName("lockoutEnabled")
        val lockoutEnabled: Boolean?,
        @SerializedName("lockoutEnd")
        val lockoutEnd: String?,
        @SerializedName("nationalCode")
        val nationalCode: String?,
        @SerializedName("normalizedEmail")
        val normalizedEmail: String?,
        @SerializedName("normalizedUserName")
        val normalizedUserName: String?,
        @SerializedName("passwordHash")
        val passwordHash: String?,
        @SerializedName("permissions")
        val permissions: List<Int?>?,
        @SerializedName("phoneNumber")
        val phoneNumber: String?,
        @SerializedName("phoneNumberConfirmed")
        val phoneNumberConfirmed: Boolean?,
        @SerializedName("profileImageName")
        val profileImageName: String?,
        @SerializedName("salt")
        val salt: String?,
        @SerializedName("securityStamp")
        val securityStamp: String?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("twoFactorEnabled")
        val twoFactorEnabled: Boolean?,
        @SerializedName("userName")
        val userName: String?,
        @SerializedName("verifyCode")
        val verifyCode: String?,
        @SerializedName("verifyCodeExpireDate")
        val verifyCodeExpireDate: String?,
        @SerializedName("verifyStatus")
        val verifyStatus: Int?,
        @SerializedName("userType")
        val userType: Int?,
    )
}

data class RegisterResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("isSuccess")
    val isSuccess: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("statusCode")
    val statusCode: Int?
) {
    data class Data(
        @SerializedName("accessFailedCount")
        val accessFailedCount: Int?,
        @SerializedName("activationCode")
        val activationCode: String?,
        @SerializedName("biography")
        val biography: String?,
        @SerializedName("birthDate")
        val birthDate: String?,
        @SerializedName("concurrencyStamp")
        val concurrencyStamp: String?,
        @SerializedName("createdDateTime")
        val createdDateTime: String?,
        @SerializedName("cridit")
        val cridit: Int?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("emailConfirmed")
        val emailConfirmed: Boolean?,
        @SerializedName("fatherName")
        val fatherName: String?,
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("forgetPasswordCode")
        val forgetPasswordCode: String?,
        @SerializedName("forgetPasswordExpireDate")
        val forgetPasswordExpireDate: String?,
        @SerializedName("gender")
        val gender: Int?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlock")
        val isBlock: Boolean?,
        @SerializedName("isDeleted")
        val isDeleted: Boolean?,
        @SerializedName("isManager")
        val isManager: Boolean?,
        @SerializedName("isVerified")
        val isVerified: Boolean?,
        @SerializedName("jwtToken")
        val jwtToken: String?,
        @SerializedName("lastLoginDate")
        val lastLoginDate: String?,
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("lastRequest")
        val lastRequest: String?,
        @SerializedName("lastRequestName")
        val lastRequestName: String?,
        @SerializedName("lockoutEnabled")
        val lockoutEnabled: Boolean?,
        @SerializedName("lockoutEnd")
        val lockoutEnd: String?,
        @SerializedName("nationalCode")
        val nationalCode: String?,
        @SerializedName("normalizedEmail")
        val normalizedEmail: String?,
        @SerializedName("normalizedUserName")
        val normalizedUserName: String?,
        @SerializedName("passwordHash")
        val passwordHash: String?,
        @SerializedName("permissions")
        val permissions: List<Int?>?,
        @SerializedName("phoneNumber")
        val phoneNumber: String?,
        @SerializedName("phoneNumberConfirmed")
        val phoneNumberConfirmed: Boolean?,
        @SerializedName("profileImageName")
        val profileImageName: String?,
        @SerializedName("salt")
        val salt: String?,
        @SerializedName("securityStamp")
        val securityStamp: String?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("twoFactorEnabled")
        val twoFactorEnabled: Boolean?,
        @SerializedName("userName")
        val userName: String?,
        @SerializedName("verifyCode")
        val verifyCode: String?,
        @SerializedName("verifyCodeExpireDate")
        val verifyCodeExpireDate: String?,
        @SerializedName("verifyStatus")
        val verifyStatus: Int?
    )
}

data class UpdateUserProfileResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("isSuccess")
    val isSuccess: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("statusCode")
    val statusCode: Int?
) {
    data class Data(
        @SerializedName("accessFailedCount")
        val accessFailedCount: Int?,
        @SerializedName("activationCode")
        val activationCode: String?,
        @SerializedName("biography")
        val biography: String?,
        @SerializedName("birthDate")
        val birthDate: String?,
        @SerializedName("concurrencyStamp")
        val concurrencyStamp: String?,
        @SerializedName("createdDateTime")
        val createdDateTime: String?,
        @SerializedName("cridit")
        val cridit: Int?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("emailConfirmed")
        val emailConfirmed: Boolean?,
        @SerializedName("fatherName")
        val fatherName: String?,
        @SerializedName("firstName")
        val firstName: String?,
        @SerializedName("forgetPasswordCode")
        val forgetPasswordCode: String?,
        @SerializedName("forgetPasswordExpireDate")
        val forgetPasswordExpireDate: String?,
        @SerializedName("gender")
        val gender: Int?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("isActive")
        val isActive: Boolean?,
        @SerializedName("isBlock")
        val isBlock: Boolean?,
        @SerializedName("isDeleted")
        val isDeleted: Boolean?,
        @SerializedName("isManager")
        val isManager: Boolean?,
        @SerializedName("isVerified")
        val isVerified: Boolean?,
        @SerializedName("jwtToken")
        val jwtToken: String?,
        @SerializedName("lastLoginDate")
        val lastLoginDate: String?,
        @SerializedName("lastName")
        val lastName: String?,
        @SerializedName("lastRequest")
        val lastRequest: String?,
        @SerializedName("lastRequestName")
        val lastRequestName: String?,
        @SerializedName("lockoutEnabled")
        val lockoutEnabled: Boolean?,
        @SerializedName("lockoutEnd")
        val lockoutEnd: String?,
        @SerializedName("nationalCode")
        val nationalCode: String?,
        @SerializedName("normalizedEmail")
        val normalizedEmail: String?,
        @SerializedName("normalizedUserName")
        val normalizedUserName: String?,
        @SerializedName("passwordHash")
        val passwordHash: String?,
        @SerializedName("permissions")
        val permissions: List<Int?>?,
        @SerializedName("phoneNumber")
        val phoneNumber: String?,
        @SerializedName("phoneNumberConfirmed")
        val phoneNumberConfirmed: Boolean?,
        @SerializedName("profileImageName")
        val profileImageName: String?,
        @SerializedName("salt")
        val salt: String?,
        @SerializedName("securityStamp")
        val securityStamp: String?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("twoFactorEnabled")
        val twoFactorEnabled: Boolean?,
        @SerializedName("userName")
        val userName: String?,
        @SerializedName("verifyCode")
        val verifyCode: String?,
        @SerializedName("verifyCodeExpireDate")
        val verifyCodeExpireDate: String?,
        @SerializedName("verifyStatus")
        val verifyStatus: Int?
    )
}
data class UserResponse(
    val count: Int?,
    val data: Data?,
    val isSuccess: Boolean?,
    val message: String?,
    val statusCode: Int?
) {
    data class Data(
        val accessFailedCount: Int?,
        val activationCode: String?,
        val biography: String?,
        val birthDate: String?,
        val concurrencyStamp: String?,
        val createdDateTime: String?,
        val cridit: Int?,
        val email: String?,
        val emailConfirmed: Boolean?,
        val fatherName: String?,
        val firstName: String?,
        val forgetPasswordCode: String?,
        val forgetPasswordExpireDate: String?,
        val gender: Int?,
        val id: String?,
        val isActive: Boolean?,
        val isBlock: Boolean?,
        val isDeleted: Boolean?,
        val isManager: Boolean?,
        val isVerified: Boolean?,
        val jwtToken: String?,
        val lastLoginDate: String?,
        val lastName: String?,
        val lastRequest: String?,
        val lastRequestName: String?,
        val lockoutEnabled: Boolean?,
        val lockoutEnd: String?,
        val nationalCode: String?,
        val normalizedEmail: String?,
        val normalizedUserName: String?,
        val passwordHash: String?,
        val permissions: List<Int?>?,
        val phoneNumber: String?,
        val phoneNumberConfirmed: Boolean?,
        val profileImageName: String?,
        val salt: String?,
        val securityStamp: String?,
        val token: String?,
        val twoFactorEnabled: Boolean?,
        val userName: String?,
        val verifyCode: String?,
        val verifyCodeExpireDate: String?,
        val verifyStatus: Int?
    )
}
data class UploadResponseDto(
    val data: List<UploadFileModelDto>? = null,
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)

data class UploadFileModelDto(
    val fullPath: String,
    val pathFName: String,
    val fileName: String,
)

data class ChangePasswordDto(
    val oldPassword: String?,
    val password: String?
)
data class GlobalResponseDto(
    val isSuccess: Boolean,
    val statusCode: Long,
    val message: String,
    val count: Long
)
data class SetUserAccessTypeForUserDto(
    val type: Int,
    val password: String
)


















