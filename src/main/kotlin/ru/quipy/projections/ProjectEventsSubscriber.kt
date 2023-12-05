package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.config.EventSourcingLibConfiguration
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.registerUser
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Service
class ProjectEventsSubscriber(
    private val projectRepository: ProjectRepository,
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {

    val logger: Logger = LoggerFactory.getLogger(ProjectEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {

        subscriptionsManager.createSubscriber(ProjectAggregate::class, "some-meaningful-name") {
            `when`(ProjectCreatedEvent::class) { event ->
                projectRepository.save(Project(event.projectId, event.title))
                logger.info("Project created: {} by user {}", event.title, event.createdBy)
            }

            `when`(StatusCreatedEvent::class) { event ->
                logger.info("Status created: {} with color {}", event.statusValue, event.statusColor)
            }

            `when`(ParticipantAddedToProjectEvent::class) { event ->
                logger.info("User {} added to project {}: ", event.userId, event.projectId)
            }

            `when`(StatusDeletedEvent::class) { event ->
                logger.info("Status {} deleted from project {}: ", event.statusId, event.id)
            }
        }
    }
}

@Document("some-meaningful-name")
data class Project(
    @Id
    val projectId: UUID,
    var projectTitle: String
)

@Repository
interface ProjectRepository : MongoRepository<Project, UUID>