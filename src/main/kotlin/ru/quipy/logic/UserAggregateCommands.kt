package ru.quipy.logic

import ru.quipy.api.UserRegisteredEvent
import ru.quipy.projections.UserAccountRepository
import java.util.*

fun UserAggregateState.registerUser(
    userId: UUID,
    nickname: String,
    name: String,
    password: String,
    existsUserAccountByUserId: (userId: UUID) -> Boolean
): UserRegisteredEvent {
    if (existsUserAccountByUserId.invoke(userId)) {
        throw IllegalArgumentException("User already exist")
    }
    return UserRegisteredEvent(userId, nickname, name, password)
}
