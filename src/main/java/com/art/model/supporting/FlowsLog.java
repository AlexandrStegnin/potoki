package com.art.model.supporting;

import com.art.model.Users;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "flows_log")
public class FlowsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "page")
    private String page;

    @Column(name = "button")
    private String buttonName;

    @Column(name = "opened_form")
    private String openedForm;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

}
