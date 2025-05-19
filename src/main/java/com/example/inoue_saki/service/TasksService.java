package com.example.inoue_saki.service;

import com.example.inoue_saki.controller.form.TasksForm;
import com.example.inoue_saki.repository.TasksRepository;

import com.example.inoue_saki.repository.entity.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TasksService {

    @Autowired
    TasksRepository tasksRepository;

    /*
     * タスクを全件取得
     */
    public List<TasksForm> findAllTasks() {
        List<Tasks> results = tasksRepository.findAll();
        List<TasksForm> tasks = setTasksForm(results);
        return tasks;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<TasksForm> setTasksForm(List<Tasks> results) {
        List<TasksForm> tasksList = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            TasksForm tasks = new TasksForm();
            Tasks result = results.get(i);
            tasks.setId(result.getId());
            tasks.setContent(result.getContent());
            tasks.setStatus(result.getStatus());
            tasks.setLimitDate(result.getLimitDate().toLocalDate());
            tasksList.add(tasks);
        }
        return tasksList;
    }

    /*
     * レコード追加
     */
    public void saveTasks(TasksForm reqTasks) {
        Tasks saveTasks = setTasksEntity(reqTasks);
        tasksRepository.save(saveTasks);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Tasks setTasksEntity(TasksForm reqTasks) {
        Tasks tasks = new Tasks();
        tasks.setId(reqTasks.getId());
        tasks.setContent(reqTasks.getContent());
        tasks.setStatus(reqTasks.getStatus());
        tasks.setLimitDate(reqTasks.getLimitDate().atTime(0,0,0));
        return tasks;
    }

    /*
     * レコード削除
     */
    public void deleteTask(Integer id) {
        tasksRepository.deleteById(id);
    }
}