package ru.quipy.api

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.logic.StatusColor
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"

@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
    val projectId: UUID,
    val title: String,
    val statusId: UUID,
    val createdBy: UUID,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = PROJECT_CREATED_EVENT,
    createdAt = createdAt,
)

const val STATUS_CREATED_EVENT = "STATUS_CREATED_EVENT"

@DomainEvent(name = STATUS_CREATED_EVENT)
class StatusCreatedEvent(
    val projectId: UUID,
    val statusId: UUID,
    val statusColor: StatusColor,
    val statusValue: String,
    createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
    name = STATUS_CREATED_EVENT,
    createdAt = createdAt
)

const val MEMBER_ADDED_TO_PROJECT_EVENT = "MEMBER_ADDED_TO_PROJECT_EVENT"

@DomainEvent(name = MEMBER_ADDED_TO_PROJECT_EVENT)
class ParticipantAddedToProjectEvent(
    val projectId: UUID,
    val userId: UUID,
    val participantId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = MEMBER_ADDED_TO_PROJECT_EVENT,
    createdAt = createdAt
)

const val STATUS_DELETED_EVENT = "STATUS_DELETED_EVENT"
@DomainEvent(name = STATUS_DELETED_EVENT)
class StatusDeletedEvent(
    val statusId: UUID,
    createdAt: Long = System.currentTimeMillis()
) : Event<ProjectAggregate>(
    name = STATUS_DELETED_EVENT,
    createdAt = createdAt
)

