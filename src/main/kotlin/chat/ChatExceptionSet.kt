package chat

class MessageAddingException(message: String) : RuntimeException()

class MessageDeletingException(message: String) : RuntimeException()

class ChatDeletingException(message: String) : RuntimeException()
