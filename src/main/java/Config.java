import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


@RequiredArgsConstructor
@Getter
public class Config {
    private final double minSpeed;
    private final double maxSpeed;
    private final int knapsackCapacity;
    private final int numOfCities;
    private double maxDistance = -1;
    private HashMap<Integer,Integer> clostesCities;

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

    public int getClosestCity(int cityId){
        if(clostesCities==null){
            initClosestMap();
        }
        return clostesCities.get(cityId);
    }

    private void initClosestMap() {
        for(City city: cities){
            int closestIndex = -1;
            double closest = getMaxDistance();
            for(int i = 0; i<getNumOfCities(); i++){
                if(i!=city.getId()){
                    double distance = getCity(i).distanceTo(city);
                    if(distance<closest) closestIndex = i;
                }
            }
            clostesCities.put(city.getId(),closestIndex);
        }
    }
}
