package com.flux.dbservice.repository.history;

import com.flux.dbservice.entity.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
