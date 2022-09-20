package edu.school21.ex02_testing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.school21.ex02_testing.utils.RoleDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@Entity @Table(name = "users", schema = "ex02_testing")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;

    @Column(name = "login", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    private String login;

    @Column(name = "password", nullable = false)
    @Type(type = "org.hibernate.type.TextType")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}