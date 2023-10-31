package ru.quipy.logic

import ru.quipy.api.UserRegisteredEvent
import java.util.*

fun UserAggregateState.registerUser(userId: UUID, nickname: String, name: String, password: String): UserRegisteredEvent {
    return UserRegisteredEvent(userId, nickname, name, password)
}
