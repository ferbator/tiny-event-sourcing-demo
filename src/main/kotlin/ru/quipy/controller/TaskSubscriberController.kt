package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.projections.Task
import ru.quipy.service.ProjectionsService
import java.util.*

@RestController
@RequestMapping("/projections")
class TaskSubscriberController(
    val projectionsService: ProjectionsService,
) {

    @GetMapping("/taskProjections")
    fun getTaskProjections() : List<Task> {
        return this.projectionsService.getAllTaskProjection();
    }

    @GetMapping("/tasks/project/{projectId}")
    fun getTasksByProject(@PathVariable projectId: UUID): List<Task> {
        return projectionsService.getTasksByProject(projectId)
    }
}