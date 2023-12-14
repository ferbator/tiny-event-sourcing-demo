package ru.quipy.logic

import ru.quipy.api.*
import java.util.*
import ru.quipy.config.EventSourcingLibConfiguration
    fun TaskAggregateState.createTaskInProject(
        id: UUID,
        title: String,
        projectId: UUID,
        statusId: UUID,
        createdBy: UUID,
        assignedToID: UUID
    ): TaskCreatedEvent {
        return TaskCreatedEvent(
            taskId = id,
            taskName = title,
            projectId = projectId,
            statusId = statusId,
            createdBy = createdBy,
            assignedToID = assignedToID
        )
    }

    fun TaskAggregateState.renameTask(newTitle: String, participantId: UUID, findAllParticipants: (projectId: UUID) -> List<UUID>): TaskRenamedEvent {
        if (!findAllParticipants.invoke(projectId).contains(participantId)){
            throw IllegalArgumentException("The user is not a participant")
        }

        return TaskRenamedEvent(
            taskId = getId(),
            newTitle = newTitle,
            participantId = participantId
        )
    }

    fun TaskAggregateState.assignTaskToUser(userId: UUID): TaskAssignedToUserEvent {
        return TaskAssignedToUserEvent(
            taskId = getId(),
            userId = userId
        )
    }

    fun TaskAggregateState.assignStatusToTask(statusId: UUID): StatusAssignedToTaskEvent {
        return StatusAssignedToTaskEvent(
            taskId = getId(),
            statusId = statusId
        )
    }

    fun TaskAggregateState.changeTaskStatus(newStatusId: UUID): TaskStatusChangedEvent {
        return TaskStatusChangedEvent(
            taskId = getId(),
            newStatusId = newStatusId
        )
    }
