package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.projections.Task
import ru.quipy.projections.UserAccount
import ru.quipy.service.ProjectionsService
import java.util.*

@RestController
@RequestMapping("/projections")
class ProjectUsersController(
    val projectionsService: ProjectionsService,
) {

    @GetMapping("/projectUsersProjections")
    fun getTaskProjections() : List<Task> {
        return this.projectionsService.getAllTaskProjection();
    }

    @GetMapping("/exists/{nickname}")
    fun existsUser(@PathVariable nickname: String): Boolean {
        return projectionsService.existsUserByNickname(nickname)
    }

    @GetMapping("/projectUsers/{projectId}")
    fun getProjectUsers(@PathVariable projectId: UUID): List<UserAccount> {
        return projectionsService.getProjectUsers(projectId)
    }
}