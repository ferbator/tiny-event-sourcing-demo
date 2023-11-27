package ru.quipy.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.projections.TaskStatus
import ru.quipy.service.ProjectionsService

@RestController
@RequestMapping("/projections")
class TaskStatusesController(
    val projectionsService: ProjectionsService,
) {
    @GetMapping("/taskStatusesProjections")
    fun getTaskStatusProjections() : List<TaskStatus> {
        return this.projectionsService.getAllTaskStatusProjection();
    }
}