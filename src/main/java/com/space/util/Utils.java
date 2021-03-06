package com.space.util;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Utils {

    private static Date eldest;

    private static Date newest;


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

    public static boolean validateShipData(Ship ship){
        return ship.getName() != null && ship.getPlanet() != null && ship.getShipType() != null && ship.getProdDate() != null
                && ship.getSpeed() != null && ship.getCrewSize() != null && ship.getName().length() <= 50 && ship.getPlanet().length() <= 50 &&
                !ship.getName().isEmpty() && !ship.getPlanet().isEmpty() && ship.getProdDate().getTime() >= 0 && ship.getCrewSize() <= 9999
                && ship.getCrewSize() >= 1 && !(ship.getSpeed() < 0.01) && !(ship.getSpeed() > 0.99) && !ship.getProdDate().before(Utils.eldest)
                && !ship.getProdDate().after(Utils.newest);
    }

    public static double calcRating(boolean isUsed, double speed, Date prodDate){
        double k = isUsed ? 0.5 : 1;
        double rating = (80 * speed * k) / (Utils.newest.getYear() - prodDate.getYear() + 1);
        return new BigDecimal(rating).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static List<Ship> sortShips(List<Ship> ships, ShipOrder order){
        if(order != null) {
            switch (order) {
                case SPEED:
                    ships.sort(Comparator.comparing(Ship::getSpeed));
                    break;
                case ID:
                    ships.sort(Comparator.comparing(Ship::getId));
                    break;
                case DATE:
                    ships.sort(Comparator.comparing(Ship::getProdDate));
                    break;
                case RATING:
                    ships.sort(Comparator.comparing(Ship::getRating));
                    break;
            }
        }
        return ships;
    }
}
