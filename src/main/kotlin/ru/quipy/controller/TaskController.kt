package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
) {

    @PostMapping("/create")
    fun createTask(@RequestBody body: TaskDTO): TaskCreatedEvent {
        return taskEsService.create {
            it.createTaskInProject(
                UUID.randomUUID(),
                body.title,
                body.projectId,
                body.statusId
            )
        }
    }

    @GetMapping("/{taskId}")
    fun getTask(@PathVariable taskId: UUID): TaskAggregateState? {
        return taskEsService.getState(taskId)
    }

    @PutMapping("/rename/{taskId}")
    fun renameTask(@PathVariable taskId: UUID, @RequestParam newTitle: String): TaskRenamedEvent {
        return taskEsService.update(taskId) { it.renameTask(taskId, newTitle) }
    }

    @PostMapping("/assign/{taskId}")
    fun assignTaskToUser(@PathVariable taskId: UUID, @RequestParam userId: UUID): TaskAssignedToUserEvent {
        return taskEsService.update(taskId) { it.assignTaskToUser(taskId, userId) }
    }

    @PostMapping("/status/{taskId}")
        fun assignStatusToTask(@PathVariable taskId: UUID, @RequestParam statusId: UUID): StatusAssignedToTaskEvent {
        return taskEsService.update(taskId) { it.assignStatusToTask(taskId, statusId) }
    }
//
//    @PutMapping("/status/{taskId}")
//    fun changeTaskStatus(@PathVariable taskId: UUID, @RequestParam newStatusId: UUID): TaskStatusChangedEvent {
//        return taskEsService.update(taskId) { it.changeTaskStatus(taskId, newStatusId) }
//    }
}

data class TaskDTO(
    val title: String,
    val projectId: UUID,
    val statusId: UUID,
)