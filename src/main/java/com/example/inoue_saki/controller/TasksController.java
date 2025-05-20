package com.example.inoue_saki.controller;


import com.example.inoue_saki.controller.form.TasksForm;
import com.example.inoue_saki.service.TasksService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TasksController {
    @Autowired
    TasksService tasksService;

    /*
     * タスク内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name = "start", required = false) LocalDate start,
                            @RequestParam(name = "end", required = false) LocalDate end,
                            @RequestParam(name = "status", required = false) Short status,
                            @RequestParam(name = "content", required = false) String content) {
        ModelAndView mav = new ModelAndView();
        // 現在日時を取得
        LocalDate now = LocalDate.now();
        // タスクを絞り込み取得
        List<TasksForm> tasksData = tasksService.findTasksByOrder(start, end, status, content);
        System.out.println("tasksDataの中身: " + tasksData);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 現在日時データオブジェクトを保管
        mav.addObject("now", now);
        // タスクデータオブジェクトを保管
        mav.addObject("tasksList", tasksData);
        return mav;
    }

    /*
     * ステータス変更処理
     */
    @PutMapping("/update-status/{id}")
    public ModelAndView updateStatus(@PathVariable Integer id,
                                     @ModelAttribute("status") short status) {
        tasksService.saveStatus(id, status);
        return new ModelAndView("redirect:/");
    }

    /*
     * タスク削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Integer id) {
        // テーブルから投稿を削除
        tasksService.deleteTask(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * タスク追加画面表示処理
     */
    @GetMapping("/new")
    public ModelAndView newTasks() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        TasksForm taskForm = new TasksForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("tasksForm", taskForm);
        return mav;
    }

    /*
     * タスク追加処理
     */
    @PostMapping("/add")
    public ModelAndView addTasks(@ModelAttribute("tasksForm") @Validated TasksForm tasksForm,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        // 投稿をテーブルに格納
        tasksService.saveTasks(tasksForm);

        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * タスク編集画面表示処理
     */
    @GetMapping({"/edit/", "/edit/{id}"})
    public ModelAndView editTasks(@PathVariable(required = false) String id,
                                  RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        TasksForm tasks = null;

        if (!StringUtils.isBlank(id) && id.matches("^[0-9]*$")) {
            int intId = Integer.parseInt(id);

            tasks = tasksService.selectTasks(intId);
        }

        if (tasks == null) {
            redirectAttributes.addFlashAttribute("errorMessages", "不正なパラメータです");
            return new ModelAndView("redirect:/");
        }

        mav.addObject("tasksForm", tasks);
        mav.setViewName("/edit");
        return mav;
    }
}
