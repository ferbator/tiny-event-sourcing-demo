package ru.quipy.logic

import ru.quipy.api.*
import java.util.*
import ru.quipy.logic.StatusColor.GREEN

fun ProjectAggregateState.createProject(id: UUID, title: String, createdBy: UUID): ProjectCreatedEvent {
    val status = createStatusInProject(GREEN, "CREATED")
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        createdBy = createdBy,
        statusId = status.statusId
    )
}

fun ProjectAggregateState.addParticipantToProject(participantId: UUID, userId: UUID): ParticipantAddedToProjectEvent {
    if (!participantsID.contains(participantId)){
        throw IllegalArgumentException("User can't add a participant")
    }
    if (participantsID.contains(userId)) {
        throw IllegalArgumentException("User already a participant")
    }

    return ParticipantAddedToProjectEvent(
        projectId = getId(),
        userId = userId,
        participantId = participantId
    )
}

fun ProjectAggregateState.createStatusInProject(color: StatusColor, value: String): StatusCreatedEvent {
    return StatusCreatedEvent(
        projectId = getId(),
        statusId = UUID.randomUUID(),
        statusColor = color,
        statusValue = value
    )
}

fun ProjectAggregateState.deleteStatus(statusId: UUID): StatusDeletedEvent {
    if (!projectStatuses.containsKey(statusId)) {
        throw IllegalArgumentException("Status doesn't exist: $statusId")
    }

    return StatusDeletedEvent(statusId)
}