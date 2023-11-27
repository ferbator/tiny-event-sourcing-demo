package ru.quipy.projections

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.quipy.api.ParticipantAddedToProjectEvent
import ru.quipy.api.ProjectAggregate
import ru.quipy.streams.AggregateSubscriptionsManager
import java.util.*
import javax.annotation.PostConstruct

@Component
class ProjectUsersSubscriber(
    private val projectUsersRepository: ProjectUsersRepository
) {

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(ProjectAggregate::class, "user-project") {

            `when`(ParticipantAddedToProjectEvent::class) { event ->
                projectUsersRepository.save(ProjectUser(event.projectId, event.userId, event.name))
            }
        }
    }
}

@Document("user-project")
data class ProjectUser(
    val projectId: UUID,
    val userId: UUID,
    val userName: String,
)

@Repository
interface ProjectUsersRepository : MongoRepository<ProjectUser, UUID>{
    fun findAllByProjectId(projectId: UUID): List<ProjectUser>
}