package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {

    List<Ship> findAllByNameContains(String name);

}
