package com.flux.dbservice.entity.history;

import com.flux.dbservice.entity.users.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "history", schema = "history_data")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Enumerated(EnumType.STRING)
    HistoryEvent event;

    String requestMessage;
    String requestDate;
    Long userChatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @PrePersist
    protected void onCreate() {
        requestDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
}
