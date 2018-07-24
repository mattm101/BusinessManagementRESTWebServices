package com.springproject27.springproject.vacation;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springproject27.springproject.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "begin_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDateOfVacation;

    @NotNull
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDateOfVacation;

    @NotEmpty
    @Column(name = "type")
    private String typeOfVacation;

    @NotEmpty
    @Column(name = "reason")
    private String reasonOfVacation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("vacations")
    @JsonBackReference
    @Builder.Default
    private User user = null;

    @JsonIgnore
    public Long getDifferenceBetweenTwoDates() {
        return DAYS.between(beginDateOfVacation, endDateOfVacation);
    }
}
