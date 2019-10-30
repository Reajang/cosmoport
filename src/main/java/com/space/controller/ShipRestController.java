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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@Controller
@RequestMapping("/rest/ships")
public class ShipRestController {

    @Autowired
    private ShipService service;

    //Получение всех кораблей. доделать параметры
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
                                                   @RequestParam(required = false) Integer pageNumber) {
        List<Ship> list = service.getShipsList();
        return list.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(list, HttpStatus.OK);
    }

    //Получение кол-ва кораблей. Добавить параметров
    @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Long getShipsCount() {
        return service.shipsCount();
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
        if(!Utils.validateId(id.toString())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship = service.findById(id);
        if(ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ship,HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> create(@RequestBody Ship ship) {
        //Как это сократить?
        if (ship.getName() == null || ship.getPlanet() == null || ship.getShipType() == null || ship.getProdDate() == null
                || ship.getSpeed() == null || ship.getCrewSize() == null || ship.getName().length() > 50 || ship.getPlanet().length() > 50 ||
                ship.getName().isEmpty() || ship.getPlanet().isEmpty() || ship.getProdDate().getTime() < 0 || ship.getCrewSize() > 9999
                || ship.getCrewSize() < 1 || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99 || ship.getProdDate().before(Utils.eldest)
                || ship.getProdDate().after(Utils.newest)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        double k = ship.getUsed() ? 0.5 : 1;
        double rating = (80 * ship.getSpeed() * k) / (Utils.newest.getYear() - ship.getProdDate().getYear() + 1);
        ship.setRating(new BigDecimal(rating).setScale(2, RoundingMode.HALF_UP).doubleValue());
        service.createShip(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }
//переделать
    @PostMapping("/{id}")
    public ResponseEntity<Ship> update(@PathVariable Long id, @RequestBody Ship ship){
        if(!Utils.validateId(id.toString())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship newShip = service.findById(id);
        if(newShip == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(ship.getName() != null) newShip.setName(ship.getName());
        if(ship.getPlanet() != null) newShip.setPlanet(ship.getPlanet());
        if(ship.getShipType() != null) newShip.setShipType(ship.getShipType());
        if(ship.getProdDate() != null) newShip.setProdDate(ship.getProdDate());
        if(ship.getUsed() != null) newShip.setUsed(ship.getUsed());
        if(ship.getSpeed() != null) newShip.setSpeed(ship.getSpeed());
        if(ship.getCrewSize() != null) newShip.setCrewSize(ship.getCrewSize());
        double k = ship.getUsed() ? 0.5 : 1;
        double rating = (80 * ship.getSpeed() * k) / (Utils.newest.getYear() - ship.getProdDate().getYear() + 1);
        ship.setRating(new BigDecimal(rating).setScale(2, RoundingMode.HALF_UP).doubleValue());
        newShip.setId(ship.getId());
        service.createShip(newShip);
        return new ResponseEntity<>(newShip, HttpStatus.OK);
    }
}
