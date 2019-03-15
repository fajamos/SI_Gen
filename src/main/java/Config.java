import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


@RequiredArgsConstructor
@Getter
public class Config {
    private final double minSpeed;
    private final double maxSpeed;
    private final int knapsackCapacity;
    private final int numOfCities;
    private double maxDistance = -1;
    @Getter private final boolean evolveItems;
    @Getter(AccessLevel.NONE) private final ArrayList<City> cities;

    public City getCity(int index){
        return cities.get(index);
    }

    public double getMaxDistance() {
        if(maxDistance != -1){
            return maxDistance;
        }
        double height = Collections.max(cities, Comparator.comparingDouble(City::getY_coord)).getY_coord() -
                Collections.min(cities, Comparator.comparingDouble(City::getY_coord)).getY_coord();
        double width = Collections.max(cities, Comparator.comparingDouble(City::getX_coord)).getX_coord() -
                Collections.min(cities, Comparator.comparingDouble(City::getX_coord)).getX_coord();
        double result = Math.sqrt(Math.pow(height,2) + Math.pow(width,2));
        maxDistance = result;
        return result;
    }
}
