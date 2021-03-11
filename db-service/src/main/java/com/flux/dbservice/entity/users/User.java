package com.flux.dbservice.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flux.dbservice.entity.history.History;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "user", schema = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    Long chatId;
    String fName;
    String lName;
    String userNickName;
    String userGroup;
    String userLanguage;
    Boolean active;
    Boolean adminEntity;
    Boolean isDefined;

    @OneToMany(fetch = FetchType.LAZY)
    List<History> history = new LinkedList<>();

    @Cascade(CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_option_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    UserOption userOption;
}
