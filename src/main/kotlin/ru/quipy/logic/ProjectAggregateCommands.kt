package ru.quipy.logic

import ru.quipy.api.*
import java.util.*
import ru.quipy.logic.StatusColor.GREEN

fun ProjectAggregateState.createProject(id: UUID, title: String, createdBy: UUID): ProjectCreatedEvent {
    val status = createStatusInProject(id, GREEN, "CREATED")
    return ProjectCreatedEvent(
        projectId = id,
        title = title,
        createdBy = UUID.randomUUID(),
        statusId = status.statusId
    )
}

fun ProjectAggregateState.addParticipantToProject(projectId: UUID, userId: UUID): ParticipantAddedToProjectEvent {
    if (this.getId() != projectId) {
        throw IllegalArgumentException("Mismatching project ID")
    }

    if (participantsID.contains(userId)) {
        throw IllegalArgumentException("User already a participant")
    }

    return ParticipantAddedToProjectEvent(
        projectId = projectId,
        userId = userId
    )
}

fun ProjectAggregateState.createStatusInProject(projectId: UUID, color: StatusColor, value: String): StatusCreatedEvent {
//    if (this.projectStatuses.isNotEmpty() && this.getId() != projectId ) {
//        throw IllegalArgumentException("Mismatching project ID")
//    }

    return StatusCreatedEvent(projectId, UUID.randomUUID(), color, value)
}

fun ProjectAggregateState.deleteStatus(statusId: UUID): StatusDeletedEvent {
    if (!projectStatuses.containsKey(statusId)) {
        throw IllegalArgumentException("Status doesn't exist: $statusId")
    }

    return StatusDeletedEvent(statusId)
}