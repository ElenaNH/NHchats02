import chat.Chat
import chat.ChatService
import chat.Message
import people.User
import people.UserNotFoundException
import people.UserService

fun main(args: Array<String>) {
    println("Hello!")

    UserService.add(User(name = "Olga"))
    UserService.add(User(name = "Sasha"))
    UserService.add(User(name = "Petya"))

//    println("Поиск:")
    val user0 = UserService.getUserById(0)
    val user1 = UserService.getUserById(1)
    val user2 = UserService.getUserById(2)
    println("Полный список пользователей:")
    println(UserService.getUserList())

    try {
        val user = UserService.getUserById(-1)
        println("User with id=-1 is $user")
    } catch (e: UserNotFoundException) {
        println("User with id=-1 is not found")
    }
    println()
    val msg0 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Hello ${user1.name}!"))
    val msg1 = ChatService.addMessage(user1, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
    val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
    val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))
    val msg4 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like violets"))
    val msg5 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "Do you like flowers?"))
    val msg6 = ChatService.addMessage(user1, Message(from = user1, to = user2, text = "Hi, Pete, I'm busy!"))

    println("До изменения:")
    ChatService.displayMessageList(ChatService.getMessagesFromChat(msg0.from, msg0.id, 0))  // 0 означает отбор всех, начиная с msg0.id

    println("После изменения:")
    ChatService.editMessageById(user0, msg0.id, "Hello dear ${user1.name}!")
    ChatService.displayMessageList(ChatService.getMessagesFromChat(msg0.from, msg0.id, 0))


    println("После изменения прочитать 2 сообщения, начиная с первого непрочитанного объекта:")
    ChatService.editMessageById(user0, msg3.id, "${msg3.text}!!!")
    var furMessageId =  ChatService.getFirstUnreadMessageIdFromChat(user1, msg3.chat)
    if (furMessageId == null)
        println("No unread messages in ${msg3.chat}")
    else {
        println("Первое непрочитанное: id=$furMessageId")
        ChatService.displayMessageList(ChatService.getMessagesFromChat(msg3.from, furMessageId, 2))
    }



    println("После удаления:")
    ChatService.deleteMessageById(msg0.from, msg3.id)
    ChatService.displayMessageList(ChatService.getMessagesFromChat(msg0.from, msg0.id, 0))

    println("+++++")
    println("For $user0:")
    println(ChatService.getUnreadChatsCount(user0))
    println(ChatService.getChats(user0))
    println("For $user1:")
    println(ChatService.getUnreadChatsCount(user1))
    println(ChatService.getChats(user1))
    println("For $user2:")
    println(ChatService.getUnreadChatsCount(user2))
    println(ChatService.getChats(user2))

    println("# # #")
    println("${user1.name} участвует в чатах:")
    var chats = ChatService.getChats(user1)
    for (chat in chats) {
        ChatService.displayChatInfo(user1, chat)
    }
    println("Саша удаляет чат Пети и Саши.У Саши остается:")
    ChatService.deleteChat(msg6.from, msg6.chat)
    chats = ChatService.getChats(user1)
    for (chat in chats) {
        ChatService.displayChatInfo(user1, chat)
    }

}