package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class TaskAggregateState : AggregateState<UUID, TaskAggregate> {
    private lateinit var taskId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var name: String
    lateinit var projectId: UUID
    lateinit var taskStatusId: UUID
    lateinit var assignedToID: UUID
    lateinit var createdBy: UUID

    override fun getId() = taskId

    @StateTransitionFunc
    fun createTaskInProject(event: TaskCreatedEvent) {
        taskId = event.taskId
        name = event.taskName
        projectId = event.projectId
        taskStatusId = event.statusId
        createdBy = event.createdBy
        assignedToID = event.assignedToID
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun taskRenamedApply(event: TaskRenamedEvent) {
        name = event.newTitle
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun assignTaskToUser(event: TaskAssignedToUserEvent) {
        assignedToID = event.userId
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun assignStatusToTask(event: StatusAssignedToTaskEvent){
        taskStatusId = event.statusId
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun changeTaskStatus(event: TaskStatusChangedEvent) {
        taskStatusId = event.newStatusId
        updatedAt = createdAt
    }
}