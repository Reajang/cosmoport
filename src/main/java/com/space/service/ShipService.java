package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ShipService {

    Ship createShip(Ship ship);

    Ship findById(Long id);

    void deleteShip(Ship ship);

    Long shipsCount();

    List<Ship> getShipsList();
    List<Ship> getShipsListByName(String name);

}
