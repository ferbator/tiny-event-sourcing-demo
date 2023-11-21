package ru.quipy.controller

import javassist.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
) {
    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam createdBy: String) : ProjectCreatedEvent {
        return projectEsService.create { it.createProject(UUID.randomUUID(), projectTitle, UUID.randomUUID()) }
    }

    @PostMapping("/{projectId}/statuses")
    fun createStatus(
        @PathVariable projectId: UUID,
        @RequestParam color: StatusColor,
        @RequestParam status: String
    ): StatusCreatedEvent {
        return projectEsService.update(projectId) { it.createStatusInProject(projectId, color, status) }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @GetMapping("/{projectId}/tasks")
    fun getAllTasks(@PathVariable projectId: UUID): ResponseEntity<Set<UUID>> {
        val state = projectEsService.getState(projectId) ?: throw NotFoundException("Project not found")
        return ResponseEntity.ok(state.getAllTasks())
    }

    @GetMapping("/{projectId}/statuses")
    fun getAllStatuses(@PathVariable projectId: UUID): ResponseEntity<Map<UUID, StatusEntity>> {
        val state = projectEsService.getState(projectId) ?: throw NotFoundException("Project not found")
        return ResponseEntity.ok(state.getAllStatuses())
    }

    @GetMapping("/{projectId}/statuses/{statusId}")
    fun getStatusById(@PathVariable projectId: UUID, @PathVariable statusId: UUID): ResponseEntity<StatusEntity> {
        val state = projectEsService.getState(projectId) ?: throw NotFoundException("Project not found")
        return ResponseEntity.ok(state.getStatusById(statusId) ?: throw NotFoundException("Status not found"))
    }

    @PostMapping("/{projectId}/participants")
    fun addParticipant(@PathVariable projectId: UUID, @RequestParam userId: UUID): ParticipantAddedToProjectEvent {
        return projectEsService.update(projectId) { it.addParticipantToProject(projectId, userId) }
    }

//    @DeleteMapping("/{projectId}")
//    fun deleteProject(@PathVariable projectId: UUID): ProjectDeletedEvent {
//        return projectEsService.update(projectId) { it.deleteProject(projectId) }
//    }

//    @DeleteMapping("/{projectId}/tasks/{taskId}")
//    fun deleteTask(@PathVariable projectId: UUID, @PathVariable taskId: UUID): TaskDeletedEvent {
//        return projectEsService.update(projectId) { it.deleteTask(taskId) }
//    }

    @DeleteMapping("/{projectId}/statuses/{statusId}")
    fun deleteStatus(@PathVariable projectId: UUID, @PathVariable statusId: UUID): StatusDeletedEvent {
        return projectEsService.update(projectId) { it.deleteStatus(statusId) }
    }
}