package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.UserAggregate
import ru.quipy.api.UserRegisteredEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.registerUser
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {
    @PostMapping("")
    fun createUser(@RequestBody body: UserDTO) : UserRegisteredEvent {
        return userEsService.create { it.registerUser(UUID.randomUUID(), body.nickname, body.name, body.password) }
    }

    @GetMapping("/{projectId}")
    fun getUser(@PathVariable projectId: UUID) : UserAggregateState? {
        return userEsService.getState(projectId)
    }
}

data class UserDTO(
    val nickname: String,
    val name: String,
    val password: String,
)