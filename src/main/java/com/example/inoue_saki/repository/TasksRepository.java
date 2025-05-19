package com.example.inoue_saki.repository;

import com.example.inoue_saki.repository.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {

    /*
     * タスク.ステータスの保存処理
     */
    @Modifying
    @Query("UPDATE Tasks t SET t.status = :status, t.updatedDate = CURRENT_TIMESTAMP WHERE t.id = :id")
    void saveStatus(@Param("status") short status, @Param("id") int id);
}
