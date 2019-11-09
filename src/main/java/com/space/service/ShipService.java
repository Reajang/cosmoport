package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {

    Ship createShip(Ship ship);

    Ship findById(Long id);

    void deleteShip(Ship ship);

    Long shipsCount();

    List<Ship> getShipsList();
    List<Ship> getShipsListByName(String name);

}
