package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import java.util.*
import ru.quipy.domain.Event



const val USER_REGISTERED_EVENT = "UserRegisteredEvent"
@DomainEvent(name = USER_REGISTERED_EVENT)
class UserRegisteredEvent(
    val userId: UUID,
    val nickname: String,
    val userName: String,
    val password: String,
    createdAt: Long = System.currentTimeMillis()
) : Event<UserAggregate>(
    name = USER_REGISTERED_EVENT,
    createdAt = createdAt
)