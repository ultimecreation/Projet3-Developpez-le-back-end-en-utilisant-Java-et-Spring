package com.chatop.backend.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    // @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<Rental> rentals;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval =
    // true)
    // private List<Message> messages;

    @JsonFormat(pattern = "yyyy-dd-MM")
    private Date created_at;

    @JsonFormat(pattern = "yyyy-dd-MM")
    private Date updated_at;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.name;
    }

}
