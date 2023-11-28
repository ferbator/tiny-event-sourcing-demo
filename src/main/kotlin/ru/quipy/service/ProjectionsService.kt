package ru.quipy.service

import org.springframework.stereotype.Service
import ru.quipy.controller.ProjectSubscriberController
import ru.quipy.controller.TaskDTO
import ru.quipy.projections.*
import java.util.UUID

@Service
class ProjectionsService (
    val userAccountRepository: UserAccountRepository,
    val taskRepository: TaskRepository,
    val projectRepository: ProjectRepository,
    val taskStatusRepository: TaskStatusRepository,
    val projectUsersRepository: ProjectUsersRepository,
){
    fun getAllUserProjection(): List<UserAccount> {
        return userAccountRepository.findAll()
    }

    fun existsUserByNickname(nickname: String): Boolean {
        return userAccountRepository.existsUserAccountsByNickname(nickname)
    }

    fun getAllTaskProjection(): List<Task> {
        return taskRepository.findAll()
    }

    fun getTasksByProject(projectId: UUID): List<Task> {
        return taskRepository.findAllByProjectId(projectId)
    }

    fun getAllProjectUserProjection(): List<ProjectUser> {
        return projectUsersRepository.findAll()
    }

    fun getProjectUsers(projectID: UUID): List<UserAccount> {
        val pairProjectUser = projectUsersRepository.findAllByProjectId(projectID)
        return userAccountRepository.getAllByUserId(pairProjectUser.map { it.userId })
    }

    fun getAllTaskStatusProjection(): List<TaskStatus> {
        return taskStatusRepository.findAllByTaskId()
    }

    fun getAllProjectProjection(): List<Project> {
        return projectRepository.findAll()
    }

    fun getProjectByID(projectID: UUID): Project {
        return projectRepository.findById(projectID).get();
    }

    fun getProjectDetails(projectID: UUID): ProjectSubscriberController.ProjectDetailsDTO {
        val project = projectRepository.findById(projectID)
            .orElseThrow { NoSuchElementException("Project not found") }

        val tasks = taskRepository.findAllByProjectId(projectID)
            .map { task -> TaskDTO(
                title = task.taskName,
                projectId = task.projectId,
                statusId = task.statusId,
                createdBy = task.createdBy,
                assignedToID = task.assignedToID
            ) }

        return ProjectSubscriberController.ProjectDetailsDTO(
            projectId = project.projectId,
            projectName = project.projectTitle,
            tasks = tasks
        )
    }

    fun getStatusByID(statusID: UUID): TaskStatus? {
        return taskStatusRepository.findByStatusId(statusID);
    }
}