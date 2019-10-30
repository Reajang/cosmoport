package com.space.util;

import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Date eldest;

    public static Date newest;


    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2800);
        eldest = calendar.getTime();
        calendar.set(Calendar.YEAR, 3019);
        newest = calendar.getTime();
    }

    public static boolean validateId(String id) {
        return id.matches("\\d+") && Long.parseLong(id) >= 0;
    }

    public static Ship getShip() {
        Ship ship = new Ship();
        ship.setId(1L);
        ship.setRating(10D);
        ship.setCrewSize(100);
        ship.setSpeed(0.5);
        ship.setUsed(true);
        ship.setProdDate(new Date());
        ship.setShipType(ShipType.MILITARY);
        ship.setPlanet("sdfd");
        ship.setName("sdsdfsdffd");
        return ship;
    }
}
