package com.example.demo.model.user;

import com.example.demo.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
@Where(clause = "archived = false")
public class UserProfile extends BaseModel {
    @Id
    @Column(name = "user_id")
    private UUID id;

    @Column
    private boolean gender;

    @Column(name = "dob")
    private Date dateOfBirth;

    @Column
    private String country;

    @Column
    private String address;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_hobbies", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "hobby")
    private List<String> hobbies = new ArrayList<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
