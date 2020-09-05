package com.staxrt.tutorial.repository;

import com.staxrt.tutorial.model.yapilacaklarliste;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface YapilacaklarRepository extends JpaRepository<yapilacaklarliste, Long> {}
