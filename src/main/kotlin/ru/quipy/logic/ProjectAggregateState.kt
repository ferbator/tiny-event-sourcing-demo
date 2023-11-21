package ru.quipy.logic

import ru.quipy.api.*
import ru.quipy.core.annotations.StateTransitionFunc
import ru.quipy.domain.AggregateState
import java.util.*

class ProjectAggregateState : AggregateState<UUID, ProjectAggregate> {
    private lateinit var projectId: UUID
    var createdAt: Long = System.currentTimeMillis()
    var updatedAt: Long = System.currentTimeMillis()

    lateinit var name: String
    lateinit var createdBy: UUID
    var participantsID = mutableSetOf<UUID>()
    var tasks = mutableSetOf<UUID>()
    var projectStatuses = mutableMapOf<UUID, StatusEntity>()

    override fun getId() = projectId

    fun getAllTasks(): Set<UUID> {
        return tasks
    }

    fun getStatusById(statusId: UUID): StatusEntity? {
        return projectStatuses[statusId]
    }

    fun getAllStatuses(): Map<UUID, StatusEntity> {
        return projectStatuses
    }
    fun getAllParticipantsID(): Set<UUID> {
        return participantsID
    }
    @StateTransitionFunc
    fun projectCreatedApply(event: ProjectCreatedEvent) {
        projectId = event.projectId
        name = event.title
        createdBy = event.createdBy
        projectStatuses[event.statusId] = StatusEntity(
            event.statusId,
            StatusColor.GREEN,
            "CREATED"
        )
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun statusCreatedApply(event: StatusCreatedEvent) {
        projectStatuses[event.statusId] = StatusEntity(
            event.statusId,
            event.statusColor,
            event.statusValue
        )
        updatedAt = createdAt
    }

    @StateTransitionFunc
    fun participantAddedToProjectApply(event: ParticipantAddedToProjectEvent) {
        if (participantsID.contains(event.userId)) throw IllegalArgumentException("User already exist: ${event.userId}")
        participantsID.add(event.userId)
        updatedAt = createdAt
    }


    @StateTransitionFunc
    fun statusDeletedApply(event: StatusDeletedEvent) {
        projectStatuses.remove(event.statusId)
        updatedAt = createdAt
    }
}

data class StatusEntity(
    val id: UUID = UUID.randomUUID(),
    val color: StatusColor,
    val status: String,
)

enum class StatusColor {
    GREEN,
    RED,
    YELLOW,
}