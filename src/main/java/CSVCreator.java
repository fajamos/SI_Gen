import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class CSVCreator {
    private final Config config;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM HH_mm_ss");

    private static final String routeFilenameBegin = "logs/route";
    private static final String itemsFilenameBegin = "logs/items";
    private static final String generationsFilenameBegin = "logs/generations";
    private static final String overallFilenameBegin = "logs/overall";


    public void exportThiefRoute(Thief thief) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer i : thief.getGenotypeCities()){
            stringBuilder.append(config.getCity(i).getCoordinates() + "\n");
        }
        stringBuilder.append(config.getCity(thief.getGenotypeCities().get(0)).getCoordinates() + "\n");
        File file = new File(routeFilenameBegin + getEndFilename());
        FileWriter fw = new FileWriter(file);
        fw.write(stringBuilder.toString());
        fw.close();
    }

    public void exportGenerationsChart(List<Double> bests,List<Double> avarges, List<Double> worst) throws IOException {
        if(!(bests.size()==avarges.size() && avarges.size()==worst.size())){
            throw new IllegalStateException();
        }

        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(generationsFilenameBegin + getEndFilename());
        FileWriter fw = new FileWriter(file);
        for(int i=0; i<bests.size(); i++){
            stringBuilder.append(bests.get(i));
            stringBuilder.append(",");
            stringBuilder.append(avarges.get(i));
            stringBuilder.append(",");
            stringBuilder.append(worst.get(i));
            stringBuilder.append("\n");
        }
        fw.write(stringBuilder.toString());
        fw.close();
    }

    public void exportThiefItems(Thief thief) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(itemsFilenameBegin + getEndFilename());
        FileWriter fw = new FileWriter(file);
        int profit = 0;
        int weight = 0;
        for(int i : thief.getGenotypeCities()){
            int itemProfit = 0;
            int itemWeight = 0;
            Item item = itemOf(i,thief.getKnapsack());
            if(item!=null){
                itemProfit = item.getPrice();
                itemWeight = item.getWeight();
            }
            weight+=itemWeight;
            profit+=itemProfit;
            stringBuilder.append(profit);
            stringBuilder.append(",");
            stringBuilder.append(weight);
            stringBuilder.append("\n");
        }
        fw.write(stringBuilder.toString());
        fw.close();
    }

    private Item itemOf(int cityId, List<Item> knapsack){
        for(Item i : knapsack){
            if(i.getCityId()==cityId) return  i;
        }
        return null;
    }

    private String getEndFilename(){
        return simpleDateFormat.format(new Date()) + ".txt";
    }
}
