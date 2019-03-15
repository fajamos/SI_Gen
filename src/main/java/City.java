import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
public class City {
    private final int id;
    private final double x_coord;
    private final double y_coord;
    private final ArrayList<Item> items = new ArrayList<Item>();

    public int getId(){
        return id-1;
    }

    public void addItem(Item item){
        items.add(item);
    }

    public double distanceTo(City city){
        return Math.sqrt(Math.pow(x_coord-city.x_coord,2) + Math.pow(y_coord-city.y_coord,2));
    }

    public List<Item> filterItems(final int capacityLeft){
        return items.stream().filter(i -> i.getWeight()<=capacityLeft).collect(Collectors.toList());
    }

    public Item getBestItem(final int capacityLeft){
        if(items.size()==0 || capacityLeft==0){
            return null;
        }
        Item result = null;
        double bestScore = 0;
        double currentScore;
        for(Item i : filterItems(capacityLeft)){
            currentScore = i.getScore();
            if(currentScore>bestScore){
                result = i;
                bestScore = currentScore;
            }
        }
        return result;
    }

    public String getCoordinates(){
        return x_coord + "," + y_coord;
    }

}
