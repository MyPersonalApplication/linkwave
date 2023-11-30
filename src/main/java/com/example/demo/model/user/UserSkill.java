package com.example.demo.model.user;

import com.example.demo.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_skills")
@Where(clause = "archived = false")
public class UserSkill extends BaseModel {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "certification_name")
    private String certificationName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
