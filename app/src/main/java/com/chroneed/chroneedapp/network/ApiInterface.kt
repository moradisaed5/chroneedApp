package com.chroneed.chroneedapp.network

import com.chroneed.chroneedapp.data.medical.*
import com.chroneed.chroneedapp.data.social.*
import com.chroneed.chroneedapp.data.user.*
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiInterface {
    //------------------------    Medical Methods    ------------------------
    @GET("api/Medical/GetMedicalGoalByUser/{skip}/{take}")
    fun getmedicalgoal(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalGoalListResponse>

    @POST("api/Medical/AddPrescription")
    fun AddPrescription(@Body requestBody: NewPrescriptionModel): Call<GetPrescriptionModelResponse>

    @PUT("api/Medical/UpdatePrescription")
    fun updatePrescription(@Body requestBody: UpdatePrescriptionModel): Call<GetPrescriptionModelResponse>

    @GET("api/Medical/SearchMedicineList/{searchText}")
    fun searchMedicineList(@Path("searchText") searchText: String): Call<GetMedicineListModelResponse>

    @GET("api/Medical/SearchMedicineListV2/{searchText}")
    fun searchMedicineListV2(@Path("searchText") searchText: String): Call<SearchMedicineListModelV2Response>

    @GET("api/Medical/GetPrescriptionByUser/{skip}/{take}")
    fun getprescriptionbyuser(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetPrescriptionListResponse>

    @GET("api/Medical/GetPrescription/{id}")
    fun getPrescription(@Path("id") id: String): Call<GetPrescriptionModelResponse>

    @DELETE("api/Medical/DeletePrescription/{id}")
    fun deletePrescription(@Path("id") id: String): Call<DeletePrescriptionResponse>

    @DELETE("api/Medical/DeleteMedicalRecord/{id}")
    fun deleteMedicalRecord(@Path("id") id: String): Call<DeleteMedicalRecordResponse>

    @GET("api/Medical/GetMedicalRecordByUser/{skip}/{take}")
    fun getMedicalRecordByUser(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalRecordListResponse>

    @GET("api/Medical/GetMedicalHabitByUser/{skip}/{take}")
    fun getMedicalHabitList(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalHabitListResponse>

    @GET("api/Medical/GetMedicalHabit/{id}")
    fun getMedicalHabit(@Path("id") id: String): Call<GetMedicalHabitResponse>

    @DELETE("api/Medical/DeleteMedicalHabit/{id}")
    fun deleteMedicalHabit(@Path("id") id: String): Call<DeleteMedicalHabitResponse>

    @POST("api/Medical/AddMedicalHabit")
    fun addMedicalHabit(@Body requestBody: NewMedicalHabitModel): Call<GetMedicalHabitResponse>

    @PUT("api/Medical/UpdateMedicalHabit")
    fun updateMedicalHabit(@Body requestBody: UpdateMedicalHabitModel): Call<GetMedicalHabitResponse>

    @GET("api/Medical/GetUserMedicineByUser/{skip}/{take}")
    fun getUserMedicineByUser(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalMedicineListResponse>

    @GET("api/Medical/GetMedicationAlarmByUser/{skip}/{take}")
    fun getMedicationAlarmByUser(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalAlarmListResponse>

    @GET("api/Medical/GetMedicationAlarm/{id}")
    fun getMedicationAlarm(@Path("id") id: String): Call<GetMedicalAlarmResponse>

    @POST("api/Medical/AddMedicationAlarm")
    fun addMedicationAlarm(@Body requestBody: MedicalAlarmModel): Call<GetMedicalAlarmResponse>

    @PUT("api/Medical/UpdateMedicationAlarm")
    fun updateMedicationAlarm(@Body requestBody: MedicalAlarmModel): Call<GetMedicalAlarmResponse>

    @DELETE("api/Medical/DeleteMedicationAlarm/{id}")
    fun deleteMedicationAlarm(@Path("id") id: String): Call<DeleteMedicalAlarmResponse>

    @GET("api/Medical/GetPractitionerByUser/{skip}/{take}")
    fun getPractitionerByUser(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetPractitionerListResponse>

    @GET("api/Medical/GetUserCarePlanByUser/{skip}/{take}")
    fun getUserCarePlanByUser(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetCarePlanListResponse>

    @GET("api/Medical/GetUserCarePlan/{id}")
    fun getUserCarePlan(@Path("id") id: String): Call<GetCarePlanResponse>

    @POST("api/Medical/AddUserCarePlan")
    fun addUserCarePlan(@Body requestBody: NewCarePlanRequest): Call<GetCarePlanResponse>

    @PUT("api/Medical/UpdateUserCarePlan")
    fun updateUserCarePlan(@Body requestBody: UpdateCarePlanRequest): Call<GetCarePlanResponse>

    @DELETE("api/Medical/DeleteUserCarePlan/{id}")
    fun deleteUserCarePlan(@Path("id") id: String): Call<DeleteCarePlanResponse>


    @GET("api/Medical/GetMedicalRecordStageList/{skip}/{take}")
    fun getMedicalRecordStageList(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalRecordStageListResponse>

    @GET("api/Medical/GetMedicalRecordTypeList/{skip}/{take}")
    fun getMedicalRecordTypeList(@Path("skip") skip: Int, @Path("take") take: Int): Call<GetMedicalRecordTypeListResponse>

    @GET("api/Medical/GetMedicalRecord/{id}")
    fun getMedicalRecord(@Path("id") id: String): Call<GetMedicalRecordResponse>

    @POST("api/Medical/AddMedicalRecord")
    fun addMedicalRecord(@Body requestBody: NewMedicalRecordRequest): Call<GetMedicalRecordResponse>

    @PUT("api/Medical/UpdateMedicalRecord")
    fun updateMedicalRecord(@Body requestBody: UpdateMedicalRecordRequest): Call<GetMedicalRecordResponse>


    //------------------------    User Methods    ------------------------

    @POST("api/User/Login")
    fun login(@Body requestBody: LoginDto): Call<LoginResponse>

    @POST("api/User/Register")
    fun register(@Body requestBody: RegisterDto): Call<RegisterResponse>

    @PUT("api/User/UpdateUserProfile")
    fun editProfile(@Body requestBody: UpdateUserProfileDto): Call<UpdateUserProfileResponse>

    @PUT("api/User/ChangeUserPassword")
    fun changeUserPassword(@Body requestBody: ChangePasswordDto): Call<UserResponse>

    //------------------------    Social Methods    ------------------------
    @GET("api/Social/GetSocialPostList/{skip}/{take}")
    fun getSocialPostList(@Path("skip") skip: Int, @Path("take") take: Int): Call<SocialPostListResponse>

    @GET("api/Social/GetUserProfile")
    fun getUserProfile(): Call<SocialProfileResponse>


    @GET("api/Social/VisitUserProfile/{id}")
    fun visitUserProfile(@Path("id") id: String): Call<SocialProfileResponse>

    @GET("api/Social/GetMySocialPostList/{skip}/{take}")
    fun getMySocialPostList(@Path("skip") skip: Int, @Path("take") take: Int): Call<SocialPostListResponse>

    @GET("api/Social/GetMyRequestList/{skip}/{take}")
    fun getMyRequestList(@Path("skip") skip: Int, @Path("take") take: Int): Call<SocialRequestListResponse>

    @GET("api/Social/VisitSocialPostList/{id}/{skip}/{take}")
    fun visitSocialPostList(@Path("id") id: String, @Path("skip") skip: Int, @Path("take") take: Int): Call<SocialPostListResponse>

    @GET("api/Medical/GetAlarmNotifyList")
    fun getAlarmNotifyList(): Call<GetAlarmNotifyListResponse>

    @GET("api/Social/GetSearchSocialUserList/{search}/{skip}/{take}")
    fun getSearchSocialUserList(@Path("search") search: String, @Path("skip") skip: Int, @Path("take") take: Int): Call<SocialSearchUserResponse>

    @GET("api/Social/GetSocialPostList/{search}/{skip}/{take}")
    fun getSocialPostList(@Path("search") search: String, @Path("skip") skip: Int, @Path("take") take: Int): Call<SocialSearchPostResponse>

    @POST("api/Social/AddSocialPost")
    fun addSocialPost(@Body requestBody: NewSocialPostModel): Call<SocialPostResponse>

    @POST("api/Social/AcceptRequestUser/{id}")
    fun acceptRequestUser(@Path("id") id: String): Call<SocialRequestResponse>

    @PUT("api/Social/RejectRequestUser/{id}")
    fun rejectRequestUser(@Path("id") id: String): Call<SocialRequestResponse>

    @POST("api/Social/SetSocialUserProfile")
    fun setSocialUserProfile(@Body requestBody: NewSocialProfileModel): Call<SocialProfileResponse>

    @POST("api/Social/AddSocialPostComment")
    fun addSocialPostComment(@Body requestBody: NewSocialCommentModel): Call<SocialCommentResponse>

    @POST("api/Medical/AddUserCommand")
    fun addUserCommand(@Body requestBody: NewCommandModel): Call<AddUserCommandResponse>

    @GET("api/Medical/GetUserCommandByUser/{skip}/{take}")
    fun getUserCommandByUser(@Path("skip") skip: Int, @Path("take") take: Int): Call<UserCommandsResponse>

    @GET("api/PyAi/GetVoices/{label}")
    fun getVoices(@Path("label") label: String): Call<GetVoiceResponseDto>

    @GET("api/PyAi/TrainModels")
    fun trainModels(): Call<TrainResponseDto>

    @PUT("api/Medical/UpdateUserCommand_TrainLabel/{id}/{newWord}")
    fun updateUserCommandTrainLabel(@Path("id") id: String, @Path("newWord") newWord: String): Call<TrainResponseDto>

    @GET("api/Medical/GetUserCommandRecognition/{word}")
    fun getUserCommandRecognition(@Path("word") word: String): Call<TrainResponseDto>

    @PUT("api/Medical/updateUserCommand_ClearTrains/{id}")
    fun updateUserCommand_ClearTrains(@Path("id") id: String): Call<TrainResponseDto>

    @DELETE("api/PyAi/DeleteUserLabelVoices/{label}/{fileName}")
    fun deleteUserLabelVoices(@Path("label") label: String, @Path("fileName") fileName: String): Call<DeleteVoiceResponseDto>

    @POST("api/Social/toggleLikeSocialPost/{id}")
    fun toggleLikeSocialPost(@Path("id") id: String): Call<Void>

    @POST("api/Social/SendRequestUser/{id}")
    fun sendRequestUser(@Path("id") id: String): Call<SocialProfileResponse>

    @POST("api/Medical/SetGoal")
    fun setGoal(@Body requestBody: SetGoalRequest): Call<SetGoalResponse>

    @GET("api/Medical/GetUserMedicalGoalStatics")
    fun getUserMedicalGoalStatics(): Call<GetGoalStaticsResponse>

    @GET("api/Social/GetSocialPostCommentList/{id}/{skip}/{take}")
    fun getSocialPostCommentList(@Path("id") id: String, @Path("skip") skip: Int, @Path("take") take: Int): Call<SocialCommentListResponse>

    @GET("api/Social/VisitSocialPostById/{id}")
    fun visitSocialPostById(@Path("id") id: String): Call<SocialPostResponse>

    @DELETE("api/Medical/DeleteUserCommand/{id}")
    fun deleteUserCommand(@Path("id") id: String): Call<DeleteCommandResponse>

    @POST("api/User/SetUserAccessTypeForUser")
    fun setUserAccessTypeForUser(@Body requestBody: SetUserAccessTypeForUserDto): Call<GlobalResponseDto>


    //------------------------    Upload Methods    ------------------------
    @GET("api/PyAi/GetTrainModelTest")
    fun downloadTrainFile(): Call<ResponseBody>


    @Multipart
    @POST("api/FileUpload/upload")
    fun uploadFile(@Part file: MultipartBody.Part?): Call<UploadResponseDto>


    @Multipart
    @POST("api/PyAi/SendVoices/{label}")
    fun sendVoices(@Path("label") label: String, @Part file: MultipartBody.Part?): Call<SendVoiceResponseDto>

    @Multipart
    @POST("api/PyAi/PrescriptionOcrV2")
    fun prescriptionOcrV2(@Part file: MultipartBody.Part?): Call<PrescriptionMedicineModel>

    public companion object {
        //                var BASE_URL = "http://chroneed.saeid-moradi.ir/"
        private var BASE_URL = "http://chroneedapi.com/identity.api/"

        fun create(token: String): ApiInterface {
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor { chain: Interceptor.Chain ->
                    val request = chain.request().newBuilder().addHeader("Authorization", token).build()
                    chain.proceed(request)
                }.build()

            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}