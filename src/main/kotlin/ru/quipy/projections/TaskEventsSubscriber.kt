package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.TaskAggregateState
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Component
class TaskEventsSubscriber(
    private val taskRepository: TaskRepository,
) {

    val logger: Logger = LoggerFactory.getLogger(TaskEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "task-subscriber") {
            `when`(TaskCreatedEvent::class) { event ->
                taskRepository.save(Task(
                    taskId = event.taskId,
                    taskName = event.taskName,
                    projectId = event.projectId,
                    statusId = event.statusId,
                    createdBy = event.createdBy,
                    assignedToID = event.assignedToID
                ))
                logger.info("Task created: {}", event.taskName)
            }

            `when`(TaskRenamedEvent::class) { event ->
                val task = taskRepository.findById(event.taskId).get()
                task.taskName = event.newTitle
                taskRepository.deleteById(task.taskId)
                taskRepository.save(task)
                logger.info("Task {} changed title to {}: ", event.taskId, event.newTitle)
            }

            `when`(TaskAssignedToUserEvent::class) { event ->
                logger.info("Task {} assigned to user {}: ", event.taskId, event.userId)
            }

            `when`(StatusAssignedToTaskEvent::class) { event ->
                logger.info("Status {} assigned to task {}: ", event.statusId, event.taskId)
            }

            `when`(TaskStatusChangedEvent::class) { event ->
                logger.info("Task {} changed status to {}: ", event.taskId, event.newStatusId)
            }
        }
    }
}

@Document("task-subscriber")
data class Task(
    @Id
    val taskId: UUID,
    var taskName: String,
    val projectId: UUID,
    val statusId: UUID,
    val createdBy: UUID,
    val assignedToID: UUID
)

@Repository
interface TaskRepository : MongoRepository<Task, UUID>{
    fun findAllByProjectId(projectID: UUID): List<Task>
}