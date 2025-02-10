package com.f.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class About {
    @Id
    private long id=1L;
    private String header;
    private String road;
    private String house;
    private String phone;
    private String zila;

    private String description;
    private String cover;
    private String Offcieimage;

    
}
