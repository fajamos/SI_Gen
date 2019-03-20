import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class Thief extends ArrayList<Integer> {
    @Getter private final ArrayList<Integer> genotypeCities;
    @Getter private final ArrayList<Integer> genotypeItems;
    private final Config config;
    @Getter private final ArrayList<Item> knapsack = new ArrayList<>();
    private double walkTime = -1;
    private int profit = -1;
    private City currentCity;
    private Double fitness = null;

    public Thief(Thief thief){
        this.config = thief.config;
        genotypeCities = new ArrayList<>(config.getNumOfCities());
        genotypeItems = new ArrayList<>(config.getNumOfCities());
        this.genotypeCities.addAll(thief.genotypeCities);
        this.genotypeItems.addAll(thief.genotypeItems);
    }

    public double getFitness(){
        double dupa =  -1;
        if(fitness==null) {
            fitness = -1 * (getTotalWalkTime() - getProfit());
        }
        return fitness;
    }

    private int getProfit(){
        if(profit!=-1){
            return profit;
        }
        int result = 0;
        for(Item i : knapsack){
            result+= i.getPrice();
        }
        profit = result;
        return result;
    }

    public double getTotalWalkTime(){
        if(walkTime !=-1){
            return walkTime;
        }
        double result = 0;
        currentCity = config.getCity(genotypeCities.get(0));
        stealItem();
        for(int i = 1; i< genotypeCities.size(); i++){
            result+= walkToNext(i);
        }
        result+=getWalkTime(config.getCity(genotypeCities.get(0)));
        walkTime = result;
        return result;
    }

    private double walkToNext(int i){
        City nextCity = config.getCity(genotypeCities.get(i));
        double result = getWalkTime(nextCity);
        currentCity = nextCity;
        stealItem();
        return result;
    }


    private double getWalkTime(City city){
       return currentCity.distanceTo(city)/getCurrentSpeed();
    }

    private double getCurrentSpeed(){
        return config.getMaxSpeed()-
                (getCurrentWeight()*((config.getMaxSpeed()-config.getMinSpeed())/(double)config.getKnapsackCapacity()));
    }

    public int getCurrentWeight(){
        int result = 0;
        for(Item i : knapsack){
            result += i.getWeight();
        }
        return result;
    }

    private void stealItem(){
        if(config.isEvolveItems()) {
            int cityId = currentCity.getId();
            if (currentCity.getItems().size() > 0) {
                int itemIndex = genotypeItems.get(cityId) - 1;
                if (itemIndex != -1) {
                    Item item = currentCity.getItems().get(itemIndex);
                    if (item.getWeight() <= config.getKnapsackCapacity() - getCurrentWeight()) {
                        knapsack.add(currentCity.getItems().get(itemIndex));
                    }
                }
            }
        }
        else {
            Item item = currentCity.getBestItem(config.getKnapsackCapacity()-getCurrentWeight());
            if(item!=null){
                knapsack.add(item);
            }
        }
    }

}
