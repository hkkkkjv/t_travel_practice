package ru.kpfu.itis.t_travel.domain.model

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String
) {
    companion object {
        fun mock() = User(
            id = 1,
            username = "ivanivanov",
            name = "Иван Иванов",
            phone = "+79990001122",
            email = "ivan@ivan.ru",
            password = "ivanivan"
        )
    }
}
