package com.example.demo.model.user;

import com.example.demo.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_experiences")
@Where(clause = "archived = false")
public class UserExperience extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "company_or_school_name")
    private String companyOrSchoolName;

    @Column(name = "position_or_degree")
    private String positionOrDegree;

    @Column
    private String description;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column
    private String location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
