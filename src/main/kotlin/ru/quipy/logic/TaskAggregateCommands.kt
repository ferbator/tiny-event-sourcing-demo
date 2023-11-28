package ru.quipy.logic

import ru.quipy.api.*
import java.util.*

//todo выпилить id из почти всех команд
fun TaskAggregateState.createTaskInProject(id: UUID, title: String, projectId: UUID, statusId: UUID): TaskCreatedEvent {
    return TaskCreatedEvent(UUID.randomUUID(), title, projectId, statusId)
}

fun TaskAggregateState.renameTask(taskId: UUID, newTitle: String): TaskRenamedEvent {
    return TaskRenamedEvent(taskId, newTitle)
}

fun TaskAggregateState.assignTaskToUser(taskId: UUID, userId: UUID): TaskAssignedToUserEvent {
    return TaskAssignedToUserEvent(taskId, userId)
}

fun TaskAggregateState.assignStatusToTask(taskId: UUID, statusId: UUID): StatusAssignedToTaskEvent {
    return StatusAssignedToTaskEvent(taskId, statusId)
}

fun TaskAggregateState.changeTaskStatus(taskId: UUID, newStatusId: UUID): TaskStatusChangedEvent {
    return TaskStatusChangedEvent(taskId, newStatusId)
}