package com.customer.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "identity")
public class Identity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be null")
    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters")
    @Column(unique = true)
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "DomainName cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9_]{10}$", message = "DomainName must be a 10-character alphanumeric string, allowing underscores only")
    @Column(unique = true)
    private String domainName;

    public Identity() {
    }

    public Identity(String username, String password, String domainName) {
        this.username = username;
        this.password = password;
        this.domainName = domainName;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
