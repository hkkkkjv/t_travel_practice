package ru.kpfu.itis.t_travel.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.kpfu.itis.t_travel.data.model.AuthResponse
import ru.kpfu.itis.t_travel.data.model.BudgetCategoryDto
import ru.kpfu.itis.t_travel.data.model.BudgetDto
import ru.kpfu.itis.t_travel.data.model.ExpenseDto
import ru.kpfu.itis.t_travel.data.model.NotificationDto
import ru.kpfu.itis.t_travel.data.model.ParticipantDto
import ru.kpfu.itis.t_travel.data.model.SettlementDto
import ru.kpfu.itis.t_travel.data.model.TripDto
import ru.kpfu.itis.t_travel.data.model.UserDto
import ru.kpfu.itis.t_travel.data.model.UserRegistrationRequest
import ru.kpfu.itis.t_travel.domain.model.LoginCredentials

interface ApiService {
    // Trip endpoints
    @GET("v1/trips")
    suspend fun getTrips(): Response<List<TripDto>>

    @GET("/v1/trips/{tripId}")
    suspend fun getTripDetails(@Path("tripId") tripId: Int): Response<TripDto>

    @POST("v1/trips")
    suspend fun createTrip(@Body trip: TripDto): Response<TripDto>

    @PUT("v1/trips/{tripId}")
    suspend fun updateTrip(@Path("tripId") tripId: Int, @Body trip: TripDto): Response<Unit>

    @DELETE("v1/trips/{tripId}")
    suspend fun deleteTrip(@Path("tripId") tripId: Int): Response<Unit>

    @GET("v1/trips/{tripId}/participants")
    suspend fun getParticipants(
        @Path("tripId") tripId: Int,
    ): Response<List<ParticipantDto>>

    @POST("v1/trips/{tripId}/participants")
    suspend fun addParticipant(
        @Path("tripId") tripId: Int,
        @Body participant: ParticipantDto
    ): Response<ParticipantDto>

    @PUT("v1/trips/{tripId}/participants/{participantId}")
    suspend fun updateParticipant(
        @Path("tripId") tripId: Int,
        @Path("participantId") participantId: Int,
        @Body participant: ParticipantDto
    ): Response<Unit>

    @DELETE("v1/trips/{tripId}/participants/{participantId}")
    suspend fun deleteParticipant(
        @Path("tripId") tripId: Int,
        @Path("participantId") participantId: Int
    ): Response<Unit>

    @PUT("v1/trips/{tripId}/participants/{participantId}/confirm")
    suspend fun confirmParticipation(
        @Path("tripId") tripId: Int,
        @Path("participantId") participantId: Int
    ): Response<Unit>

    // Budget endpoints
    @GET("v1/trips/{tripId}/budget")
    suspend fun getBudget(@Path("tripId") tripId: Int): Response<BudgetDto>

    @POST("v1/trips/{tripId}/budget")
    suspend fun setBudget(
        @Path("tripId") tripId: Int,
        @Body budget: BudgetDto
    ): Response<BudgetDto>

    @PUT("v1/trips/{tripId}/budget")
    suspend fun updateBudget(
        @Path("tripId") tripId: Int,
        @Body budget: BudgetDto
    ): Response<Unit>

    @GET("v1/trips/{tripId}/budget/categories")
    suspend fun getBudgetCategories(@Path("tripId") tripId: Int): Response<List<BudgetCategoryDto>>

    @POST("v1/trips/{tripId}/budget/categories")
    suspend fun addBudgetCategory(
        @Path("tripId") tripId: Int,
        @Body category: BudgetCategoryDto
    ): Response<BudgetCategoryDto>

    @PUT("v1/trips/{tripId}/budget/categories/{category}")
    suspend fun updateBudgetCategory(
        @Path("tripId") tripId: Int,
        @Path("category") category: String,
        @Body updatedCategory: BudgetCategoryDto
    ): Response<BudgetCategoryDto>

    @DELETE("v1/trips/{tripId}/budget/categories/{category}")
    suspend fun deleteBudgetCategory(
        @Path("tripId") tripId: Int,
        @Path("category") category: String
    ): Response<Unit>

    // Expense endpoints
    @GET("v1/trips/{tripId}/expenses")
    suspend fun getExpenses(@Path("tripId") tripId: Int): Response<List<ExpenseDto>>

    @POST("v1/trips/{tripId}/expenses")
    suspend fun addExpense(
        @Path("tripId") tripId: Int,
        @Body expense: ExpenseDto
    ): Response<ExpenseDto>

    @DELETE("v1/trips/{tripId}/expenses/{expenseId}")
    suspend fun deleteExpense(
        @Path("tripId") tripId: Int,
        @Path("expenseId") expenseId: Int
    ): Response<Unit>

    // Settlement endpoints
    @GET("v1/trips/{tripId}/settlements")
    suspend fun getSettlements(@Path("tripId") tripId: Int): Response<SettlementDto>

    @POST("v1/register")
    suspend fun register(@Body request: UserRegistrationRequest): UserDto

    @POST("v1/login")
    suspend fun login(@Body credentials: LoginCredentials): Response<AuthResponse>

    @GET("v1/user/profile")
    suspend fun getUserProfile(): Response<UserDto>

    @PUT("v1/user/profile")
    suspend fun updateUserProfile(@Body user: UserDto): Response<Unit>

    @GET("v1/user/notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @DELETE("v1/user/notifications")
    suspend fun clearNotifications(): Response<Unit>

    @POST("v1/logout")
    suspend fun logout(): Response<Unit>
}