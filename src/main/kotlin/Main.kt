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
    /*
        println("User with id=0 is $user0")
        println("User with id=1 is $user1")
        println("User with id=2 is $user2")
    */
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
    val msg1 = ChatService.addMessage(user0, Message(from = user1, to = user0, text = "Hello ${user0.name}!"))
    val msg2 = ChatService.addMessage(user2, Message(from = user2, to = user1, text = "Hi ${user1.name}!"))
    val msg3 = ChatService.addMessage(user0, Message(from = user0, to = user1, text = "I like roses"))

    println("For $user0:")
    println(ChatService.getUnreadChatsCount(user0))
    println(ChatService.getChats(user0))
    println("For $user1:")
    println(ChatService.getUnreadChatsCount(user1))
    println(ChatService.getChats(user1))
    println("For $user2:")
    println(ChatService.getUnreadChatsCount(user2))
    println(ChatService.getChats(user2))


}