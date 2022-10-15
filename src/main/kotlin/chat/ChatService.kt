package chat

import people.*

object ChatService {
    private var listMessages = mutableListOf<Message>()

    // Сообщения

    fun addMessage(user: User, message: Message): Message {
        if (user.id != message.from.id)
            throw MessageNoAccessException("Access of $user is not enough for adding message from ${message.from}")
        // Будем назначать такой id, который равен индексу в массиве
        val newMsgId = listMessages.size
        listMessages.add(message.copy(id = newMsgId, read = false, deleted = false))
        return listMessages[newMsgId]
    }

    // Поскольку чат полностью определяется любым своим сообщением (связан с парой пользователей),
    // то нет смысла передавать этот чат в качестве параметра, если уже передано начальное сообщение
    fun getMessagesFromChat(user: User, messageFromId: Int, messagesCount: Int): List<Message> {
        var counter = if (messagesCount <= 0) listMessages.size else messagesCount // Все или не все сообщения брать
        val founMessage = specialFullAccessGetMessageById(messageFromId) // Если не будет ошибки, то получим сообщение
        if (!(user memberOf founMessage.chat))
            throw MessageNoAccessException("Access of $user is not enough for reading message #$messageFromId")
        // Если доступ у пользователя есть, то продолжаем
        val selectedMessages = listMessages.filter { (!it.deleted) && (user memberOf it.chat) && (it.id >= messageFromId) }
        var resultMessages = mutableListOf<Message>()
        for (msg in selectedMessages) {
            if (counter-- <= 0) break // Если отсчитали нужно количество сообщений, то выходим
            resultMessages.add(listMessages[msg.id].copy())    // Отбираем мы все сообщения - исходящие и входящие
            if (listMessages[msg.id].to == user)
                listMessages[msg.id] = listMessages[msg.id].copy(read = true) /* Сообщения, предназначенные
                                              данному пользователю, делаем прочитанными уже после отбора */
        }
        return resultMessages.toList()    // Вернем с информацией о непрочитанности, а в приватном списке они уже будут прочитаны
    }

    fun editMessageById(user: User, messageId: Int, messageText: String): Boolean {
        // несуществующие и удаленные не редактируем
        val oldMessage = specialFullAccessGetMessageById(messageId)  // Либо получим, либо вылетим по ошибке
        if (user.id != oldMessage.from.id)
            throw MessageNoAccessException("Access of $user is not enough for editing message from ${oldMessage.from}")
        // Если с доступом к редактированию и с самим сообщением все в порядке, то редактируем
        listMessages[messageId] = oldMessage.copy(
            text = messageText,  // Редактировать можно только текст
            read = false    // После редактирования становится непрочитанным
        )
        return true
    }

    fun deleteMessageById(user: User, messageId: Int) {
        val theMessage = specialFullAccessGetMessageById(messageId)    // Получаем без учета прав доступа
        if (!(user memberOf theMessage.chat))
            throw MessageNoAccessException("Access of $user is not enough for deleting message of ${theMessage.chat}")
        // Если доступ достаточный, то помечаем сообщение удаленным
        listMessages[messageId] = theMessage.copy(deleted = true)
    }

    fun getFirstUnreadMessageIdFromChat(user: User, chat: Chat): Int? {
        if (user memberOf chat) {
            return listMessages
                .filter { (!it.deleted) && (!it.read) && (user == it.to) }
                .firstOrNull()
                ?.id
        } else {
            return null
        }
    }

    private fun specialFullAccessGetMessageById(messageId: Int): Message {
        // Нельзя делать общедоступным - нет проверки прав доступа!!!
        if ((messageId < 0) || (messageId >= listMessages.size))
            throw MessageNotFoundException("No live message with id=$messageId in ChatService")
        return listMessages[messageId]
    }

    // Чаты

    fun deleteChat(user: User, chat: Chat) {
        if (!(user memberOf chat))
            throw MessageNoAccessException("Access of $user is not enough for deleting messages from $chat")
        // Если доступ есть, то все удаляем из чата
        val chatMessages = listMessages.filter { (!it.deleted) && (chat == it.chat) }
        for (msg in chatMessages) listMessages[msg.id] = msg.copy(deleted = true)
    }

    fun getUnreadChatsCount(user: User): Int {
        return calculateChats(user, true).size
    }

    fun getChats(user: User, unreadOnly: Boolean = false): List<Chat> {
        var chts = calculateChats(user, unreadOnly).toMutableList()
        // Когда последнее сообщение находится внутри чата, то оно не становится прочитанным, а остается каким было
        for (cht in chts) cht.lastMessage = listMessages.filter { (!it.deleted) && (it.chat == cht) }.lastOrNull()
        return chts.toList()
    }

    fun displayChatInfo(user: User, chat: Chat) {
        if (user memberOf chat) {
            println(chat)
            println("""/${chat.lastMessage?.text ?: "No messages"}/""")
        }
    }

    fun displayMessageList(messages: List<Message>) {
        for (message in messages) {
            println(message)
        }
    }

    private fun calculateChats(user: User, unreadOnly: Boolean = false): List<Chat> {
        val userChats = listMessages
            .filter { (!it.deleted) && (if (unreadOnly) !it.read else true) && (user memberOf it.chat) }
            .map { it.chat }
            .toSet()
            .toList()
        return userChats
    }

}
