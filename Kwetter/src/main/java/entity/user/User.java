package entity.user;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity(name = "USERDATA")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Size(min=3, max=40)
    @Pattern(regexp = "[^a-zA-Z0-9_]+$")
    @Column(name = "USERNAME", unique = true)
    private String username;

    @Size(min=3, max=40)
    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private UserRole role;
}
