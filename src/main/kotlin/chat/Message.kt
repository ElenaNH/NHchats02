package chat

import people.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Message(
    val from: User,
    val to: User,
    val text: String,
    val id: Int = -1,
    val read: Boolean = false,
    val deleted: Boolean = false
) {
    val chat
        get() = Chat(UserPairSorted(from, to)) // Это наш чат, строго связанный с парой пользователей (порядок не важен)
    val lastModified = LocalDateTime.now()

    override fun toString(): String {
        return if (deleted) {
            ""
        } else {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.YY HH:mm:ss")
            val dateFormatted = lastModified.format(formatter)
            val unread = if (read) "" else "*"

            //${chat.userPairSorted}
            "${unread}Message $id from ${from.name} to ${to.name} $dateFormatted\n$text\n-----"
        }
    }
}
