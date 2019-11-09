package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {


    private ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Transactional(readOnly = true)
    public Long shipsCount() {
        return shipRepository.count();
    }

    @Override
    public List<Ship> getShipsList() {
        return shipRepository.findAll();
    }

    @Override
    public List<Ship> getShipsListByName(String name) {
        return shipRepository.findAllByNameContains(name);
    }

    @Transactional
    public Ship findById(Long id) {
        return shipRepository.findById(id).get();
    }

    @Override
    public void deleteShip(Ship ship) {
        shipRepository.delete(ship);
    }

    @Override
    public Ship createShip(Ship ship) {
            shipRepository.save(ship);
        return ship;
    }
}
