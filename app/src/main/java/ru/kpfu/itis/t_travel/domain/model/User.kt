package ru.kpfu.itis.t_travel.domain.model

data class User(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String
) {
    companion object {
        fun mock() = User(
            id = 1,
            username = "ivanivanov",
            firstName = "Иван",
            lastName = "Иванов",
            phone = "+79990001122",
            email = "ivan@ivan.ru",
            password = "ivanivan"
        )
    }
}
