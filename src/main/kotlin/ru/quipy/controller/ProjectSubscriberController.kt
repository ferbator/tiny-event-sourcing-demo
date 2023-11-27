package ru.quipy.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    @GetMapping("/projects/name/{projectID}")
    fun getProjectByName(@PathVariable projectID: UUID): Project {
        return projectionsService.getProjectByID(projectID)
    }
    @GetMapping("/projects/{projectId}")
    fun getProjectDetails(@PathVariable projectId: UUID): ResponseEntity<ProjectDetailsDTO> {
        val projectDetails = projectionsService.getProjectDetails(projectId)
        return ResponseEntity.ok(projectDetails)
    }

    data class ProjectDetailsDTO(
        val projectId: UUID,
        val projectName: String,
        val tasks: List<TaskDTO>
    )
}