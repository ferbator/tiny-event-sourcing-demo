package ru.quipy.service

import org.springframework.stereotype.Service
import ru.quipy.projections.*

@Service
class ProjectionsService (
    val userAccountRepository: UserAccountRepository,
    val taskRepository: TaskRepository,
    val projectRepository: ProjectRepository,
    ){
    fun getAllUserProjection(): List<UserAccount> {
        return userAccountRepository.findAll();
    }

    fun getAllTaskProjection(): List<Task> {
        return taskRepository.findAll();
    }

    fun getAllProjectProjection(): List<Project> {
        return projectRepository.findAll();
    }
}