package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class UserAggregateState : AggregateState<UUID, UserAggregate> {
    private lateinit var userId: UUID;
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var nickname: String
    lateinit var name: String
    lateinit var password: String

    override fun getId() = userId

    @StateTransitionFunc
    fun registerUserApply(event: UserRegisteredEvent) {
        userId = event.userId
        nickname = event.nickname
        name = event.userName
        password = event.password
        updatedAt = createdAt
    }
}