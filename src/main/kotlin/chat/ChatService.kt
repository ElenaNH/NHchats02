package chat

import people.*

object ChatService {
    private var listMessages = mutableListOf<Message>()

    fun addMessage(user: User, message: Message): Message {
        if (!(user memberOf message.chat)) {
            throw MessageAddingException("Access of $user is not enough for adding to ${message.chat}")
        }
        // Будем назначать такой id, который равен индексу в массиве
        val newMsgId = listMessages.size
        listMessages.add(message.copy(id = newMsgId, read = false, deleted = false))
        return listMessages[newMsgId]
    }

    fun getUnreadChatsCount(user: User): Int {
        return calculateChats(user, true).size
    }

    fun getChats(user: User): List<Chat> {
        var chts = calculateChats(user).toMutableList()
        for (cht in chts) {
            cht.lastMessage = listMessages.filter { (!it.deleted) && (it.chat == cht) }.lastOrNull()
        }
        return chts.toList()
    }

    private fun calculateChats(user: User, unreadOnly: Boolean = false): List<Chat> {
        val userChats = listMessages
            .filter { (!it.deleted) && (if (unreadOnly) !it.read else true) && (user memberOf it.chat) }
            .map {it.chat}
            .toSet()
            .toList()
        return userChats
    }

}
