package ru.quipy.projections

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.*
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Component
class TasksStatusesSubscriber(
    private val taskStatusRepository: TaskStatusRepository
) {

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(TaskAggregate::class, "task-status") {

            `when`(StatusAssignedToTaskEvent::class) { event ->
                val status = withContext(Dispatchers.IO) {
                    taskStatusRepository.findByStatusId(event.statusId)
                }
                    ?: throw Exception()
                val task = withContext(Dispatchers.IO) {
                    taskStatusRepository.findByTaskId(event.taskId)
                } ?: throw Exception()
                task.statusId = event.statusId
                task.statusValue = status.statusValue
                task.taskUpdatedAt = System.currentTimeMillis();
                taskStatusRepository.save(task);
            }

            `when`(TaskRenamedEvent::class) { event ->
                val taskStatus = withContext(Dispatchers.IO) {
                    taskStatusRepository.findByTaskId(event.taskId)
                } ?: throw Exception()
                taskStatus.taskTitle = event.newTitle;
                taskStatus.taskUpdatedAt = System.currentTimeMillis();
                taskStatusRepository.save(taskStatus);
            }

            `when`(TaskCreatedEvent::class) { event ->
                taskStatusRepository.save(TaskStatus(event.statusId, null, event.taskId, event.createdAt,
                    null, event.taskName, event.projectId, null))
            }
        }

        subscriptionsManager.createSubscriber(ProjectAggregate::class, "project-task-status") {

            `when`(StatusCreatedEvent::class) { event ->
                taskStatusRepository.save(TaskStatus(event.statusId, event.statusValue, null, event.createdAt, null, null, event.projectId, null))
            }

        }
    }
}

@Document("task-status")
data class TaskStatus(
    var statusId: UUID?,
    var statusValue: String?,
    var taskId: UUID?,
    var taskCreatedAt: Long?,
    var taskUpdatedAt: Long?,
    var taskTitle: String?,
    var projectId: UUID,
    var assignedUserId: UUID? = null
)

@Repository
interface TaskStatusRepository : MongoRepository<TaskStatus, UUID> {
    fun findByTaskId(taskId: UUID): TaskStatus?
    fun findByStatusId(statusId: UUID): TaskStatus?
    fun findAllByTaskId(): List<TaskStatus>
}