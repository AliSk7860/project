package com.poc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.entity.CountryEntity;

public interface CountryRepo extends JpaRepository<CountryEntity, Integer> {

}
