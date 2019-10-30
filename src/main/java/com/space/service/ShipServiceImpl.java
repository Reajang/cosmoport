package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Transactional
    public Long shipsCount() {
        return shipRepository.count();
    }

    @Override
    public List<Ship> getShipsList() {
        return shipRepository.findAll();
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
    public Ship updateShip(Long id, Ship ship) {
        shipRepository.deleteById(id);
        ship.setId(id);
        shipRepository.save(ship);
        return ship;
    }

    @Override
    public Ship createShip(Ship ship) {
            shipRepository.saveAndFlush(ship);
        return ship;
    }
}
