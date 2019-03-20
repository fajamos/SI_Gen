import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomLevelCreator {
    private static final int numOfCities = 15;
    private static final double maxXCoord = 2000;
    private static final double maxYCoord = maxXCoord;
    private static final int numOfItems = 100;
    private static final double minSpeed = 0.1;
    private static final double maxSpeed = 1;
    private static final int knapsackCapacity = 2000;
    private static final int maxItemWeight = 500;
    private static final int maxItemProfit = 750;
    private static final double profitWeightCorelation = 0.5;

    static int itemID = 1;
    static int cityID = 1;

    private static final String levelName  = "custom_newbie_1";

    public static void main(String[] Args) throws IOException {
        File file = new File("student/" + levelName + ".ttp");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(generateInfo());
        fileWriter.flush();
        for(int i=0; i<numOfCities; i++){
            fileWriter.write(generateCity());
        }
        fileWriter.write("\n");
        fileWriter.flush();
        for(int i=0; i<numOfItems; i++){
            fileWriter.write(generateItem());
        }
        fileWriter.flush();
        fileWriter.close();
    }

    private static String generateCity(){
        return cityID++ + " " + Math.floor(Math.random()*maxXCoord) + " " + Math.floor(Math.random()*maxYCoord) + "\n";
    }

    private static String generateItem(){
        int weight = (int) Math.ceil(Math.random() * maxItemWeight);
        int profit = (int) ((maxItemProfit*(((1-profitWeightCorelation)*Math.random())+(profitWeightCorelation*((double)weight)/maxItemWeight)))/2);
        return itemID++ + " " + profit + " " + weight + " " + (int)Math.ceil((Math.random()*numOfCities)) + "\n";
    }

    private static String generateInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\n");
        stringBuilder.append("numOfCities\t" + numOfCities + "\n");
        stringBuilder.append("numOfItems\t" + numOfItems + "\n");
        stringBuilder.append("knapsackCapacity\t" + knapsackCapacity + "\n");
        stringBuilder.append("minSpeed\t" + minSpeed + "\n");
        stringBuilder.append("maxSpeed\t" + maxSpeed + "\n");
        stringBuilder.append("\n\n\n");
        return stringBuilder.toString();
    }
}
