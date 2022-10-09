package chat

import people.*

import people.UserPairSorted

data class Chat(val userPairSorted: UserPairSorted, var lastMessage: Message? = null) {
    override fun toString(): String {
        return "Chat of ${this.userPairSorted.minIdUser.name} and ${this.userPairSorted.maxIdUser.name}"
    }
}

public infix fun User.memberOf(chat: Chat): Boolean {
    return (this == chat.userPairSorted.minIdUser) || (this == chat.userPairSorted.maxIdUser)
}
