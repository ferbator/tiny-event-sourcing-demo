package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.UserAggregate
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.projections.UserAccount
import ru.quipy.service.ProjectionsService
import java.util.*

@RestController
@RequestMapping("/projections")
class UserSubscriberController(
    val projectionsService: ProjectionsService,
) {
    @GetMapping("/userProjections")
    fun getUserProjections() : List<UserAccount> {
        return this.projectionsService.getAllUserProjection();
    }
}