package chat

import people.*

import people.UserPairSorted

data class Chat(val userPairSorted: UserPairSorted, var lastMessage: Message? = null) {
    // lastMessage может меняться и не влияет на постоянную часть (пару пользователей)
    override fun equals(other: Any?): Boolean {
        return when {
            (other == null) -> false
            (other is Chat) -> (this.userPairSorted == other.userPairSorted)
            else -> false
        }
    }

    override fun hashCode(): Int {
        return this.userPairSorted.hashCode()   // Зависит только от пары участников чата
    }

    override fun toString(): String {
        return "Chat of ${this.userPairSorted.minIdUser.name} and ${this.userPairSorted.maxIdUser.name}"
    }
}

public infix fun User.memberOf(chat: Chat): Boolean {
    return (this == chat.userPairSorted.minIdUser) || (this == chat.userPairSorted.maxIdUser)
}
