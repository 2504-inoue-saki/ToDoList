package com.example.inoue_saki.service;

import com.example.inoue_saki.controller.form.TasksForm;
import com.example.inoue_saki.repository.TasksRepository;
import com.example.inoue_saki.repository.entity.Tasks;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TasksService {

    @Autowired
    TasksRepository tasksRepository;

    /*
     * レコード絞り込み取得処理
     */
    public List<TasksForm> findTasksByOrder(LocalDate start, LocalDate end, Short status, String content) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        /*
         * 絞り込み開始日と終了日の設定
         * 開始日：未入力の場合は2020-01-01 00:00:00
         * 終了日：未入力の場合は2100-12-31 23:59:59
         */
        if (start != null) {
            startDateTime = start.atStartOfDay();
        } else {
            startDateTime = LocalDate.of(2020, 1, 1).atStartOfDay();
        }
        if (end != null) {
            endDateTime = end.plusDays(1).atStartOfDay().minusSeconds(1);
        } else {
            endDateTime = LocalDate.of(2101, 1, 1).atStartOfDay().minusSeconds(1);
        }

        /*
         * 検索条件.ステータスおよび検索条件.タスク内容の組み合わせによって使用するSQLを変える
         * 詳細はTasksRepositoryを参照
         */
        if (status != null && !StringUtils.isBlank(content)) {
            List<Tasks> results = tasksRepository.findByLimitDateBetweenAndStatusAndContentOrderByLimitDateAsc(startDateTime, endDateTime, status, content);
            return setTasksForm(results);
        } else if (status != null) {
            List<Tasks> results = tasksRepository.findByLimitDateBetweenAndStatusOrderByLimitDateAsc(startDateTime, endDateTime, status);
            return setTasksForm(results);
        } else if (!StringUtils.isBlank(content)) {
            List<Tasks> results = tasksRepository.findByLimitDateBetweenAndContentOrderByLimitDateAsc(startDateTime, endDateTime, content);
            return setTasksForm(results);
        } else {
            List<Tasks> results = tasksRepository.findByLimitDateBetweenOrderByLimitDateAsc(startDateTime, endDateTime);
            return setTasksForm(results);
        }
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<TasksForm> setTasksForm(List<Tasks> results) {
        List<TasksForm> tasksList = new ArrayList<>();

        for (Tasks value : results) {
            TasksForm tasks = new TasksForm();
            tasks.setId(value.getId());
            tasks.setContent(value.getContent());
            tasks.setStatus(value.getStatus());
            tasks.setLimitDate(value.getLimitDate().toLocalDate());
            tasksList.add(tasks);
        }
        return tasksList;
    }

    /*
     * ステータス変更
     */
    @Transactional
    public void saveStatus(Integer id, short status) {
        tasksRepository.saveStatus(status, id);
    }

    /*
     * レコード追加
     */
    public void saveTasks(TasksForm reqTasks) {
        Tasks saveTasks = setTasksEntity(reqTasks);
        // 新規作成時のステータスは1（未着手）
        if (saveTasks.getStatus() == 0) {
            saveTasks.setStatus((short) 1);
        }
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
        tasks.setLimitDate(reqTasks.getLimitDate().atTime(0, 0, 0));
        return tasks;
    }

    /*
     * レコード削除
     */
    public void deleteTask(Integer id) {
        tasksRepository.deleteById(id);
    }

    /*
     * idからレコードを取得
     */
    public TasksForm selectTasks(Integer id) {
        List<Tasks> results = new ArrayList<>();
        results.add((Tasks) tasksRepository.findById(id).orElse(null));
        if (results.get(0) != null) {
            List<TasksForm> tasks = setTasksForm(results);
            return tasks.get(0);
        } else {
            return null;
        }
    }
}