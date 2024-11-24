package com.chatop.backend.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rentals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    private String name;

    private int surface;
    private int price;

    @Column(length = 255)
    private String picture;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    // @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    // @JsonIgnore
    private List<Message> messages;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM")
    private LocalDate created_at;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-dd-MM")
    private LocalDate updated_at;

}