package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/rest/ships")
public class ShipRestController {

    @Autowired
    private ShipService service;

    //Получение всех кораблей. доделать параметры
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Ship>> getShipsList(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String planet,
                                                   @RequestParam(required = false) ShipType shipType,
                                                   @RequestParam(required = false) Long after,
                                                   @RequestParam(required = false) Long before,
                                                   @RequestParam(required = false) Boolean isUsed,
                                                   @RequestParam(required = false) Double minSpeed,
                                                   @RequestParam(required = false) Double maxSpeed,
                                                   @RequestParam(required = false) Integer minCrewSize,
                                                   @RequestParam(required = false) Integer maxCrewSize,
                                                   @RequestParam(required = false) Double minRating,
                                                   @RequestParam(required = false) Double maxRating,
                                                   @RequestParam(required = false) ShipOrder order,
                                                   @RequestParam(required = false, defaultValue = "3") Integer pageSize,
                                                   @RequestParam(required = false, defaultValue = "0") Integer pageNumber) {
        List<Ship> list = service.getShipsList();
        if (name != null) list.removeIf(ship -> !ship.getName().contains(name));
        if (planet != null) list.removeIf(ship -> !ship.getPlanet().contains(planet));
        if (shipType != null) list.removeIf(ship -> !ship.getShipType().equals(shipType));
        if (after != null) list.removeIf(ship -> ship.getProdDate().before(new Date(after)));
        if (before != null) list.removeIf(ship -> ship.getProdDate().after(new Date(before)));
        if (isUsed != null) list.removeIf(ship -> !ship.getUsed().equals(isUsed));
        if (minSpeed != null) list.removeIf(ship -> ship.getSpeed() < minSpeed);
        if (maxSpeed != null) list.removeIf(ship -> ship.getSpeed() > maxSpeed);
        if (minCrewSize != null) list.removeIf(ship -> ship.getCrewSize() < minCrewSize);
        if (maxCrewSize != null) list.removeIf(ship -> ship.getCrewSize() > maxCrewSize);
        if (minRating != null) list.removeIf(ship -> ship.getRating() < minRating);
        if (maxRating != null) list.removeIf(ship -> ship.getRating() > maxRating);

        Utils.sortShips(list, order);

        return list.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(list, HttpStatus.OK);
    }

    //Получение кол-ва кораблей. Добавить параметров
    @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    Long getShipsCount(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String planet,
                       @RequestParam(required = false) ShipType shipType,
                       @RequestParam(required = false) Long after,
                       @RequestParam(required = false) Long before,
                       @RequestParam(required = false) Boolean isUsed,
                       @RequestParam(required = false) Double minSpeed,
                       @RequestParam(required = false) Double maxSpeed,
                       @RequestParam(required = false) Integer minCrewSize,
                       @RequestParam(required = false) Integer maxCrewSize,
                       @RequestParam(required = false) Double minRating,
                       @RequestParam(required = false) Double maxRating) {
        //Как заменить?
        List<Ship> list = service.getShipsList();
        if (name != null) list.removeIf(ship -> !ship.getName().contains(name));
        if (planet != null) list.removeIf(ship -> !ship.getPlanet().contains(planet));
        if (shipType != null) list.removeIf(ship -> !ship.getShipType().equals(shipType));
        if (after != null) list.removeIf(ship -> ship.getProdDate().before(new Date(after)));
        if (before != null) list.removeIf(ship -> ship.getProdDate().after(new Date(before)));
        if (isUsed != null) list.removeIf(ship -> !ship.getUsed().equals(isUsed));
        if (minSpeed != null) list.removeIf(ship -> ship.getSpeed() < minSpeed);
        if (maxSpeed != null) list.removeIf(ship -> ship.getSpeed() > maxSpeed);
        if (minCrewSize != null) list.removeIf(ship -> ship.getCrewSize() < minCrewSize);
        if (maxCrewSize != null) list.removeIf(ship -> ship.getCrewSize() > maxCrewSize);
        if (minRating != null) list.removeIf(ship -> ship.getRating() < minRating);
        if (maxRating != null) list.removeIf(ship -> ship.getRating() > maxRating);
        return (long) list.size();
        //return service.shipsCount();
    }

    //Удаление корабля по id
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") String id) {
        if (!Utils.validateId(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            Ship ship = service.findById(Long.parseLong(id));
            service.deleteShip(ship);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Поиск по id одного корабля.
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> getShip(@PathVariable("id") Long id) {
        if (!Utils.validateId(id.toString())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship = service.findById(id);
        if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    //Добавление корабля
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> create(@RequestBody Ship ship) {
        //Как это сократить?
        if (!Utils.validateShipData(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ship.setRating(Utils.calcRating(ship.getUsed(), ship.getSpeed(), ship.getProdDate()));
        service.createShip(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    //Изменение сущуствующего корабля
    @PostMapping("/{id}")
    public ResponseEntity<Ship> update(@PathVariable Long id, @RequestBody Ship ship) {
        if (!Utils.validateId(id.toString())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Ship newShip = service.findById(id);
        if (newShip == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (ship.getName() != null) newShip.setName(ship.getName());
        if (ship.getPlanet() != null) newShip.setPlanet(ship.getPlanet());
        if (ship.getShipType() != null) newShip.setShipType(ship.getShipType());
        if (ship.getProdDate() != null) newShip.setProdDate(ship.getProdDate());
        if (ship.getUsed() != null) newShip.setUsed(ship.getUsed());
        if (ship.getSpeed() != null) newShip.setSpeed(ship.getSpeed());
        if (ship.getCrewSize() != null) newShip.setCrewSize(ship.getCrewSize());
        newShip.setRating(Utils.calcRating(ship.getUsed(), ship.getSpeed(), ship.getProdDate()));

        newShip.setId(id);
        service.createShip(newShip);
        return new ResponseEntity<>(newShip, HttpStatus.OK);
    }
}
