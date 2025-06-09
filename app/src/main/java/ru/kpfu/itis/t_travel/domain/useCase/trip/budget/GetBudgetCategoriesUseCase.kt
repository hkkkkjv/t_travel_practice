package ru.kpfu.itis.t_travel.domain.useCase.trip.budget

import ru.kpfu.itis.t_travel.domain.model.BudgetCategoryLookup
import ru.kpfu.itis.t_travel.domain.repository.TripRepository
import javax.inject.Inject

class GetBudgetCategoriesUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(): List<BudgetCategoryLookup> =
        tripRepository.getBudgetCategories()
}