package com.f.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.f.backend.entity.About;

@Repository
public interface AboutRepository extends JpaRepository<About,Long> {
    
}
