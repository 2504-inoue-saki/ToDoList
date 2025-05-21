package com.example.inoue_saki.repository;

import com.example.inoue_saki.repository.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {

    /*
     * タスク期限、ステータス、タスク内容で絞り込み
     */
    @Query
    List<Tasks> findByLimitDateBetweenAndStatusAndContentOrderByLimitDateAsc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") Short status,
            @Param("content") String content
    );

    /*
     * タスク期限、ステータスで絞り込み
     */
    @Query
    List<Tasks> findByLimitDateBetweenAndStatusOrderByLimitDateAsc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") Short status
    );

    /*
     * タスク期限、タスク内容で絞り込み
     */
    @Query
    List<Tasks> findByLimitDateBetweenAndContentOrderByLimitDateAsc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("content") String content
    );

    /*
     * タスク期限で絞り込み
     */
    @Query
    List<Tasks> findByLimitDateBetweenOrderByLimitDateAsc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    /*
     * ステータスの保存処理
     */
    @Modifying
    @Query("UPDATE Tasks t SET t.status = :status, t.updatedDate = CURRENT_TIMESTAMP WHERE t.id = :id")
    void saveStatus(@Param("status") short status, @Param("id") int id);
}
