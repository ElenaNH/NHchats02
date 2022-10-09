package people

data class User(val name: String, val id: Long = -1)

class UserPairSorted(user1: User, user2: User) {
    val minIdUser = if (user1.id <= user2.id) user1 else user2
    val maxIdUser = if (user1.id <= user2.id) user2 else user1

    override fun toString(): String {
        return "PairSorted=($minIdUser; $maxIdUser)"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is UserPairSorted) {
            ((this.minIdUser == other.minIdUser) && (this.maxIdUser == other.maxIdUser))
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = minIdUser.hashCode()
        result = 31 * result + maxIdUser.hashCode()
        return result
    }
}


object UserService {
    private var nextUserId: Long = 0
    private var listUsers = mutableListOf<User>()
    private fun getNextUserId(): Long = nextUserId++

    fun add(user: User): Long {
        val registeredUser = user.copy(id = getNextUserId())
        if (!listUsers.add(registeredUser)) {
            throw UserAddingException("Unexpected cannot add $user")
        }

        return registeredUser.id
    }

    fun getUserList(): List<User> {
        return listUsers.toList()   // Возвращаем неизменяемый список, чтобы извне его нельзя было испортить
    }

    fun getUserById(userId: Long): User {
        val userIdFilter = listUsers.filter { it.id == userId }
        if (userIdFilter.isEmpty()) {
            throw UserNotFoundException("Cannot find user by id=$userId")
        }
        // Учитывая наш способ добавления пользователей, больше одного элемента в списке не будет
        // Тем более, что пока мы их создаем навсегда, не удаляем
        return userIdFilter[0]
    }

}
