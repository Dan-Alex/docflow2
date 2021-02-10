package com.alexdan.docflow.services;

import com.alexdan.docflow.data.DocumentRepository;
import com.alexdan.docflow.data.TaskRepository;
import com.alexdan.docflow.data.UserRepository;
import com.alexdan.docflow.exceptions.TaskNotFoundException;
import com.alexdan.docflow.models.Task;
import com.alexdan.docflow.models.TasksStatuses;
import com.alexdan.docflow.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       DocumentRepository documentRepository,
                       UserRepository userRepository){

        this.taskRepository = taskRepository;
        this.documentRepository= documentRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllIncomingTasks(User user) {

        return userRepository.findById(user.getId()).get().getIncomingTasks();
    }

    public List<Task> getAllOutgoingTasks(User user){

        return  userRepository.findById(user.getId()).get().getOutgoingTasks().
                                                            stream().
                                                            filter(task -> task.getStatus() == TasksStatuses.COMPLETED).
                                                            collect(Collectors.toList());
    }

    public Task getTask(long id){

        return taskRepository.findById(id).
                orElseThrow(()-> new TaskNotFoundException(id));
    }

    public Task saveTask(Task task){

        Task savedTask = taskRepository.save(task);
        task.getDocuments().forEach(document -> {
            document.setTask(savedTask);
            documentRepository.save(document);
        });

        return savedTask;
    }

}
