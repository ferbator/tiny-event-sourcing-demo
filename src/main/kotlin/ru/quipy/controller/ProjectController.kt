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
    fun createProject(@PathVariable projectTitle: String, @RequestParam createdBy: UUID) : ProjectCreatedEvent {
        return projectEsService.create { it.createProject(UUID.randomUUID(), projectTitle, createdBy) }
    }

    @PostMapping("/{projectId}/statuses")
    fun createStatus(
        @PathVariable projectId: UUID,
        @RequestParam color: StatusColor,
        @RequestParam status: String
    ): StatusCreatedEvent {
        return projectEsService.update(projectId) { it.createStatusInProject(color, status) }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId) //todo shine2: -projectEsService.getState
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

    @PostMapping("/{projectId}/participants/{userId}/by/{participantId}")
    fun addParticipant(@PathVariable projectId: UUID, @PathVariable userId: UUID, @PathVariable participantId: UUID): ParticipantAddedToProjectEvent {
        return projectEsService.update(projectId) { it.addParticipantToProject(participantId, userId) }
    }

    @DeleteMapping("/{projectId}/statuses/{statusId}")
    fun deleteStatus(@PathVariable projectId: UUID, @PathVariable statusId: UUID): StatusDeletedEvent {
        return projectEsService.update(projectId) { it.deleteStatus(statusId) }
    }
}