import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileLoader {
    @Getter private final String filename;

    @Getter private ArrayList<City> cities;
    @Getter private Config config;
    private int numOfCities;
    private int numOfItems;
    @Setter private boolean onlyWalk = false;
    private final boolean evolveItems;
    private boolean loaded = false;


    public void load() throws IOException {
        if(loaded){
            throw new IllegalStateException("Already loaded from this loader");
        }
        int capacity;
        double minSpeed;
        double maxSpeed;
        BufferedReader reader = new BufferedReader(new FileReader("student/"  + filename));
        reader.readLine();
        reader.readLine();
        numOfCities = Integer.parseInt(reader.readLine().split("\t+")[1]);
        numOfItems = Integer.parseInt(reader.readLine().split("\t+")[1]);
        capacity = Integer.parseInt(reader.readLine().split("\t+")[1]);
        minSpeed = Double.parseDouble(reader.readLine().split("\t+")[1]);
        maxSpeed = Double.parseDouble(reader.readLine().split("\t+")[1]);

        cities = new ArrayList<>(numOfCities);

        reader.readLine();
        reader.readLine();
        reader.readLine();

        for(int i = 0; i< numOfCities; i++){
            List<Integer> city_params = Arrays.stream(reader.readLine().split("\\s+"))
                    .map(Double::parseDouble).map(Double::intValue)
                    .collect(Collectors.toList());
            cities.add(new City(city_params.get(0),city_params.get(1),city_params.get(2)));
        }

        reader.readLine();

        for(int i = 0; i< numOfItems; i++){
            List<Integer> item_params = Arrays.stream(reader.readLine().split("\\s+"))
                    .map(Double::parseDouble).map(Double::intValue)
                    .collect(Collectors.toList());
            Item item = new Item(item_params.get(0),item_params.get(2),item_params.get(1),item_params.get(3));
            cities.get(item_params.get(3)-1).addItem(item);
        }
        if(onlyWalk){
            config = new Config(maxSpeed,maxSpeed,1, numOfCities,false,cities);
        }
        else{
            config = new Config(minSpeed,maxSpeed,capacity, numOfCities,evolveItems,cities);
        }

        loaded = true;
    }
}
