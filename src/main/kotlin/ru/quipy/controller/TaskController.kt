package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import ru.quipy.service.ProjectionsService
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
    val projectionsService: ProjectionsService,
) {

    @PostMapping("/create")
    fun createTask(@RequestBody body: TaskDTO): TaskCreatedEvent {
        return taskEsService.create {
            it.createTaskInProject(
                UUID.randomUUID(),
                body.title,
                body.projectId,
                body.statusId,
                body.createdBy,
                body.assignedToID
            )
        }
    }

    @GetMapping("/{taskId}")
    fun getTask(@PathVariable taskId: UUID): TaskAggregateState? {
        return taskEsService.getState(taskId)
    }

    @PutMapping("/rename/{taskId}/by/{participantId}")
    fun renameTask(
        @PathVariable taskId: UUID,
        @RequestParam newTitle: String,
        @PathVariable participantId: UUID
    ): TaskRenamedEvent {
        return taskEsService.update(taskId) { it.renameTask(
            newTitle,
            participantId
        ) { projectId -> projectionsService.findAllByProjectId(projectId) } }
    }

    @PostMapping("/assign/{taskId}")
    fun assignTaskToUser(@PathVariable taskId: UUID, @RequestParam userId: UUID): TaskAssignedToUserEvent {
        return taskEsService.update(taskId) { it.assignTaskToUser(userId) }
    }

    @PostMapping("/status/{taskId}")
    fun assignStatusToTask(@PathVariable taskId: UUID, @RequestParam statusId: UUID): StatusAssignedToTaskEvent {
        return taskEsService.update(taskId) { it.assignStatusToTask(statusId) }
    }
}

data class TaskDTO(
    val title: String,
    val projectId: UUID,
    val statusId: UUID,
    val createdBy: UUID,
    val assignedToID: UUID
)