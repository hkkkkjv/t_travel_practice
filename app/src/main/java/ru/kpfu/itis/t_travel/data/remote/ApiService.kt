package ru.kpfu.itis.t_travel.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.data.model.BudgetCategoryDto
import ru.kpfu.itis.t_travel.data.model.BudgetCategoryLookupDto
import ru.kpfu.itis.t_travel.data.model.BudgetDto
import ru.kpfu.itis.t_travel.data.model.DeviceTokenRequest
import ru.kpfu.itis.t_travel.data.model.ExpenseDto
import ru.kpfu.itis.t_travel.data.model.NotificationDto
import ru.kpfu.itis.t_travel.data.model.ParticipantDto
import ru.kpfu.itis.t_travel.data.model.ParticipantPhoneRequest
import ru.kpfu.itis.t_travel.data.model.SettlementDto
import ru.kpfu.itis.t_travel.data.model.SettlementItemDto
import ru.kpfu.itis.t_travel.data.model.TripDto
import ru.kpfu.itis.t_travel.data.model.UserDto
import ru.kpfu.itis.t_travel.data.model.UserRegistrationRequest
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials

interface ApiService {
    // Trip endpoints
    @GET("v1/trips")
    suspend fun getTrips(): List<TripDto>

    @GET("/v1/trips/pending")
    suspend fun getPendingTrips(): List<TripDto>

    @GET("/v1/trips/confirmed")
    suspend fun getConfirmedTrips(): List<TripDto>

    @GET("/v1/trips/{tripId}")
    suspend fun getTripDetails(@Path("tripId") tripId: Int): TripDto

    @POST("v1/trips")
    suspend fun createTrip(@Body trip: TripDto): TripDto

    @PUT("v1/trips/{tripId}")
    suspend fun updateTrip(@Path("tripId") tripId: Int, @Body trip: TripDto)

    @DELETE("v1/trips/{tripId}")
    suspend fun deleteTrip(@Path("tripId") tripId: Int)

    @GET("v1/trips/{tripId}/participants")
    suspend fun getParticipants(
        @Path("tripId") tripId: Int,
    ): List<ParticipantDto>

    @POST("v1/trips/{tripId}/participants")
    suspend fun addParticipant(
        @Path("tripId") tripId: Int,
        @Body participant: ParticipantPhoneRequest
    ): ParticipantDto

    @PUT("v1/trips/{tripId}/participants/{participantId}")
    suspend fun updateParticipant(
        @Path("tripId") tripId: Int,
        @Path("participantId") participantId: Int,
        @Body participant: ParticipantDto
    )

    @DELETE("v1/trips/{tripId}/participants/{participantId}")
    suspend fun deleteParticipant(
        @Path("tripId") tripId: Int,
        @Path("participantId") participantId: Int
    )

    @PUT("v1/trips/{tripId}/participants/confirm")
    suspend fun confirmParticipation(
        @Path("tripId") tripId: Int
    )

    @PUT("v1/trips/{tripId}/participants/cancel")
    suspend fun rejectParticipation(
        @Path("tripId") tripId: Int
    )

    // Budget endpoints
    @GET("v1/trips/{tripId}/budget")
    suspend fun getBudget(@Path("tripId") tripId: Int): BudgetDto

    @POST("v1/trips/{tripId}/budget")
    suspend fun setBudget(
        @Path("tripId") tripId: Int,
        @Body budget: BudgetDto
    ): BudgetDto

    @PUT("v1/trips/{tripId}/budget")
    suspend fun updateBudget(
        @Path("tripId") tripId: Int,
        @Body budget: BudgetDto
    )

    @GET("v1/trips/{tripId}/budget/categories")
    suspend fun getBudgetCategories(@Path("tripId") tripId: Int): List<BudgetCategoryDto>

    @POST("v1/trips/{tripId}/budget/categories")
    suspend fun addBudgetCategory(
        @Path("tripId") tripId: Int,
        @Body category: BudgetCategoryDto
    ): BudgetCategoryDto

    @PUT("v1/trips/{tripId}/budget/categories/{category}")
    suspend fun updateBudgetCategory(
        @Path("tripId") tripId: Int,
        @Path("category") category: String,
        @Body updatedCategory: BudgetCategoryDto
    ): BudgetCategoryDto

    @DELETE("v1/trips/{tripId}/budget/categories/{category}")
    suspend fun deleteBudgetCategory(
        @Path("tripId") tripId: Int,
        @Path("category") category: String
    )

    // Expense endpoints
    @GET("v1/trips/{tripId}/expenses")
    suspend fun getExpenses(@Path("tripId") tripId: Int): List<ExpenseDto>

    @GET("v1/trips/{tripId}/expenses/me")
    suspend fun getMyExpenses(@Path("tripId") tripId: Int): List<ExpenseDto>

    @POST("v1/trips/{tripId}/expenses")
    suspend fun addExpense(
        @Path("tripId") tripId: Int,
        @Body expense: ExpenseDto
    ): ExpenseDto

    @DELETE("v1/trips/{tripId}/expenses/{expenseId}")
    suspend fun deleteExpense(
        @Path("tripId") tripId: Int,
        @Path("expenseId") expenseId: Int
    )

    // Settlement endpoints
    @GET("v1/trips/{tripId}/settlements")
    suspend fun getSettlements(@Path("tripId") tripId: Int): SettlementDto

    @GET("v1/trips/{tripId}/settlements/payable")
    suspend fun getPayableSettlements(@Path("tripId") tripId: Int): List<SettlementItemDto>

    @GET("v1/trips/{tripId}/settlements/receivable")
    suspend fun getReceivableSettlements(@Path("tripId") tripId: Int): List<SettlementItemDto>

    @PUT("v1/trips/{tripId}/settlements/{settlementId}/request-confirmation")
    suspend fun requestDebtConfirmation(
        @Path("tripId") tripId: Int,
        @Path("settlementId") settlementId: Int
    )

    @PUT("v1/trips/{tripId}/settlements/{settlementId}/confirm")
    suspend fun confirmDebtReturn(
        @Path("tripId") tripId: Int,
        @Path("settlementId") settlementId: Int
    )

    //User endpoints
    @POST("v1/register")
    suspend fun register(@Body request: UserRegistrationRequest): UserDto

    @POST("v1/login")
    suspend fun login(@Body credentials: LoginCredentials): AuthResponse

    @POST("v1/refresh")
    suspend fun refreshTokens(@Body body: Map<String, String>): AuthResponse

    @GET("v1/user/profile")
    suspend fun getUserProfile(): UserDto

    @PUT("v1/user/profile")
    suspend fun updateUserProfile(@Body user: UserDto)

    @GET("v1/user/notifications")
    suspend fun getNotifications(): List<NotificationDto>

    @DELETE("v1/user/notifications")
    suspend fun clearNotifications()

    @GET("v1/budget/categories")
    suspend fun getBudgetCategories(): List<BudgetCategoryLookupDto>

    @POST("v1/logout")
    suspend fun logout(@Body body: Map<String, String>)

    @GET("/v1/user/id")
    suspend fun getUserId(): Int

    @POST("/v1/device-token")
    suspend fun registerDeviceToken(@Body request: DeviceTokenRequest)

}