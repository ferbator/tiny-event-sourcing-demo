package ru.quipy.projections

import org.slf4j.Logger
import org.springframework.stereotype.Component
import ru.quipy.api.UserAggregate
import java.util.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.quipy.api.UserRegisteredEvent
import ru.quipy.streams.AggregateSubscriptionsManager
import javax.annotation.PostConstruct


@Component
class UserEventsSubscriber(
    private val userAccountRepository: UserAccountRepository,
) {

    val logger: Logger = LoggerFactory.getLogger(UserEventsSubscriber::class.java)

    @Autowired
    lateinit var subscriptionsManager: AggregateSubscriptionsManager

    @PostConstruct
    fun init() {
        subscriptionsManager.createSubscriber(UserAggregate::class, "users-subscriber") {
            `when`(UserRegisteredEvent::class) { event ->
                userAccountRepository.save(UserAccount(event.userId, event.userName, event.nickname))
                logger.info("User {} registered with name {} & nickname {}", event.userId, event.nickname, event.nickname)
            }
        }
    }
}

@Document("users-subscriber")
data class UserAccount(
    @Id
    val userId: UUID,
    var userName: String,
    var nickname: String
)

@Repository
interface UserAccountRepository : MongoRepository<UserAccount, UUID>
{
    fun getAllByUserId(userIds: List<UUID>): List<UserAccount>
    fun existsUserAccountsByNickname(nickname: String): Boolean

    fun existsUserAccountByUserId(userId: UUID): Boolean
    fun findByUserId(userId: UUID): UserAccount
}