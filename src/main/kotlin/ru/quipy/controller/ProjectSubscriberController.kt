package ru.quipy.controller

import javassist.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.ProjectAggregate
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.StatusEntity
import ru.quipy.projections.Project
import ru.quipy.service.ProjectionsService
import java.util.*

@RestController
@RequestMapping("/projections")
class ProjectSubscriberController(
    val projectionsService: ProjectionsService,
) {
    @GetMapping("/projectProjections")
    fun getProjectProjections(): List<Project> {
        return this.projectionsService.getAllProjectProjection();
    }
}