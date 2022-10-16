package chat

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import people.*

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear() // Собственно очистка чатов

        // Однократное создание группы пользователей для тестов
        // Самих пользователей мы тестировали в ранее сделанных ДЗ
        // Поэтому сейчас мы их заново тестировать не будем, а создадим нескольких перед первым вызовом первого теста
        if (UserService.getUserList().isEmpty()) {
            UserService.add(User(name = "Olga"))
            UserService.add(User(name = "Sasha"))
            UserService.add(User(name = "Petya"))
        }
    }

    @Test(expected = MessageNoAccessException::class)
    fun addMessage_NoAccess() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user2, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))

        assertNull(msg0)
//        assertFalse(msg0 is Message)
    }

    @Test
    fun addMessage() {
        val (user0, user1, _) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))

        assertTrue(msg0 is Message)
    }

    @Test(expected = MessageNotFoundException::class)
    fun getMessagesFromChat_NoMessage() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val msg6 = ChatService.addMessage(user1, Message(from = user1, to = user2, text = "Hi, Pete, I'm busy!"))

        val result = ChatService.getMessagesFromChat(msg3.from, -1, 2).size

        assertEquals(0, result)
    }

    @Test(expected = MessageNoAccessException::class)
    fun getMessagesFromChat_NoUser() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val msg6 = ChatService.addMessage(user1, Message(from = user1, to = user2, text = "Hi, Pete, I'm busy!"))

        val result = ChatService.getMessagesFromChat(user2, msg1.id, 2).size

        assertEquals(2, result)
    }

    @Test
    fun getMessagesFromChat() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val msg6 = ChatService.addMessage(user1, Message(from = user1, to = user2, text = "Hi, Pete, I'm busy!"))
        val result = ChatService.getMessagesFromChat(msg3.from, msg1.id, 2).size

        assertEquals(2, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageById_NoMessage() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))

        val newText = "Hello dear ${user1.name}!"
        ChatService.editMessageById(user0, -1, newText)
        val result = ChatService.getMessagesFromChat(user0, msg0.id, 1)[0].text

        assertEquals(newText, result)
    }

    @Test(expected = MessageNoAccessException::class)
    fun editMessageById_NoAccess() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))

        val newText = "Hello dear ${user1.name}!"
        ChatService.editMessageById(user2, msg0.id, newText)
        val result = ChatService.getMessagesFromChat(user0, msg0.id, 1)[0].text

        assertEquals(newText, result)
    }

    @Test
    fun editMessageById() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))

        val newText = "Hello dear ${user1.name}!"
        ChatService.editMessageById(user0, msg0.id, newText)
        val result = ChatService.getMessagesFromChat(user0, msg0.id, 1)[0].text

        assertEquals(newText, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageById_NoMessage() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val countBefore = ChatService.getMessagesFromChat(user0, msg0.id, 0).size
        ChatService.deleteMessageById(user0, -1)
        val countAfter = ChatService.getMessagesFromChat(user0, msg0.id, 0).size

        assertEquals(countAfter + 1, countBefore)
    }

    @Test(expected = MessageNoAccessException::class)
    fun deleteMessageById_NoAccess() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val countBefore = ChatService.getMessagesFromChat(user0, msg0.id, 0).size
        ChatService.deleteMessageById(user2, msg0.id)
        val countAfter = ChatService.getMessagesFromChat(user0, msg0.id, 0).size

        assertEquals(countAfter + 1, countBefore)
    }

    @Test
    fun deleteMessageById() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val countBefore = ChatService.getMessagesFromChat(user0, msg0.id, 0).size
        ChatService.deleteMessageById(user0, msg0.id)
        val countAfter = ChatService.getMessagesFromChat(user0, msg0.id, 0).size

        assertEquals(countAfter + 1, countBefore)
    }

    @Test
    fun getFirstUnreadMessageIdFromChat_NullOfNoAccess() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        ChatService.getMessagesFromChat(user1, msg0.id, 0)
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val result = ChatService.getFirstUnreadMessageIdFromChat(user2, msg0.chat)

        assertNull(result)
    }

    @Test
    fun getFirstUnreadMessageIdFromChat_NullOfNoMessage() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        ChatService.getMessagesFromChat(user1, msg0.id, 0)
        val result = ChatService.getFirstUnreadMessageIdFromChat(user1, msg0.chat)

        assertNull(result)
    }

    @Test
    fun getFirstUnreadMessageIdFromChat() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        ChatService.getMessagesFromChat(user1, msg0.id, 0)
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val result = ChatService.getFirstUnreadMessageIdFromChat(user1, msg0.chat)

        assertEquals(4, result)
    }

    @Test(expected = MessageNoAccessException::class)
    fun deleteChat_NoAccess() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val msg6 = ChatService.addMessage(user1, Message(from = user1, to = user2, text = "Hi, Pete, I'm busy!"))

        val chatCountBefore = ChatService.getChats(user1).size
        ChatService.deleteChat(user0, msg2.chat)
        val chatCountAfter = ChatService.getChats(user1).size

        assertEquals(chatCountBefore, chatCountAfter) // Если бы мы обработали ошибку, то было бы равно
    }

    @Test
    fun deleteChat() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val msg6 = ChatService.addMessage(user1, Message(from = user1, to = user2, text = "Hi, Pete, I'm busy!"))

        val chatCountBefore = ChatService.getChats(user1).size
        ChatService.deleteChat(user1, msg2.chat)
        val chatCountAfter = ChatService.getChats(user1).size

        assertEquals(chatCountBefore - 1, chatCountAfter)
    }

    @Test
    fun getUnreadChatsCount_NoUnreadChats() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        ChatService.getMessagesFromChat(user1, msg0.id, 0) // Один чат прочитан
        ChatService.getMessagesFromChat(user1, msg2.id, 0) // Второй чат прочитан
        val result = ChatService.getUnreadChatsCount(user1)   // не должно быть непрочитанных

        assertEquals(0, result)
    }

    @Test
    fun getUnreadChatsCount() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val readOneChat = ChatService.getMessagesFromChat(user1, msg0.id, 0) // Один чат прочитан
        val result = ChatService.getUnreadChatsCount(user1)   // еще один чат остается непрочитанным

        assertEquals(1, result)
    }

    @Test
    fun getChats_UnreadOnly() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val readOneChat = ChatService.getMessagesFromChat(user1, msg0.id, 0) // Один чат прочитан
        val result = ChatService.getChats(user1, true).size

        assertEquals(1, result)
    }

    @Test
    fun getChats() {
        val (user0, user1, user2) = UserService.getUserList()
        val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
        val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
        val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
        val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
        val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
        val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
        val readOneChat = ChatService.getMessagesFromChat(user1, msg0.id, 0) // Один чат прочитан
        val result = ChatService.getChats(user1).size

        assertEquals(2, result)
    }


}