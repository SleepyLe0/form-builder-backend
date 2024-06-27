package project.formbuilderbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "username", nullable = false, length = 20)
    private String username;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "role", nullable = false)
    private String role;
}