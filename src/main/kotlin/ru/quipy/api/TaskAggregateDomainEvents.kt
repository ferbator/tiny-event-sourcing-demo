package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import java.util.*

const val TASK_CREATED_EVENT = "TASK_CREATED_EVENT"

@DomainEvent(name = TASK_CREATED_EVENT)
class TaskCreatedEvent(
    val taskId: UUID,
    val taskName: String,
    val projectId: UUID,
    val statusId: UUID,
    val createdBy: UUID,
    val assignedToID: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<TaskAggregate>(
    name = TASK_CREATED_EVENT,
    createdAt = createdAt
)

const val TASK_RENAMED_EVENT = "TASK_RENAMED_EVENT"

@DomainEvent(name = TASK_RENAMED_EVENT)
class TaskRenamedEvent(
    val taskId: UUID,
    val newTitle: String,
    val participantId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<TaskAggregate>(
    name = TASK_RENAMED_EVENT,
    createdAt = createdAt
)

const val TASK_ASSIGNED_TO_USER_EVENT = "TASK_ASSIGNED_TO_USER_EVENT"

@DomainEvent(name = TASK_ASSIGNED_TO_USER_EVENT)
class TaskAssignedToUserEvent(
    val taskId: UUID,
    val userId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<TaskAggregate>(
    name = TASK_ASSIGNED_TO_USER_EVENT,
    createdAt = createdAt
)

//const val TASK_SELF_ASSIGNED_EVENT = "TASK_SELF_ASSIGNED_EVENT"
//
//@DomainEvent(name = TASK_SELF_ASSIGNED_EVENT)
//class TaskSelfAssignedEvent(
//    val taskId: UUID,
//    val userId: UUID,
//    createdAt: Long = System.currentTimeMillis()
//) : Event<TaskAggregate>(
//    name = TASK_SELF_ASSIGNED_EVENT,
//    createdAt = createdAt
//)

const val STATUS_ASSIGNED_TO_TASK_EVENT = "STATUS_ASSIGNED_TO_TASK_EVENT"

@DomainEvent(name = STATUS_ASSIGNED_TO_TASK_EVENT)
class StatusAssignedToTaskEvent(
    val taskId: UUID,
    val statusId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<TaskAggregate>(
    name = STATUS_ASSIGNED_TO_TASK_EVENT,
    createdAt = createdAt
)

const val TASK_STATUS_CHANGED_EVENT = "TASK_STATUS_CHANGED_EVENT"

@DomainEvent(name = TASK_STATUS_CHANGED_EVENT)
class TaskStatusChangedEvent(
    val taskId: UUID,
    val newStatusId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<TaskAggregate>(
    name = TASK_STATUS_CHANGED_EVENT,
    createdAt = createdAt
)