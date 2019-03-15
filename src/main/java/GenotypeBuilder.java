import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
public class GenotypeBuilder {
    private final Config config;
    private final double mutationProb;
    private final double crossProb;


    private static final double swapWage = 2;
    private static final double reverseWage = 0.5;
    private static final double closestWage = 8;
    private static final double sumWage = swapWage+reverseWage+closestWage;

    public ArrayList<Thief> randomGeneration(int populationSize){
        ArrayList<Thief> result = new ArrayList<>(populationSize);
        for(int i=0; i<populationSize; i++){
            result.add(randomThief());
        }
        return result;
    }

    public Thief[] cross(Thief thief1, Thief thief2) {
        Thief[] result = new Thief[2];
        if(Math.random()<crossProb){
            ArrayList<Integer>[] crossedGenotypesCities = PMX(thief1,thief2);
            result[0] = new Thief(crossedGenotypesCities[0],thief1.getGenotypeItems(),config);
            result[1] = new Thief(crossedGenotypesCities[1],thief2.getGenotypeItems(),config);
        }
        else{
            result[0] = new Thief(thief1.getGenotypeCities(),thief1.getGenotypeItems(),config);
            result[1] = new Thief(thief2.getGenotypeCities(),thief2.getGenotypeItems(),config);
        }
        if(Math.random()<crossProb) {
            crossItemsGenotype(result[0].getGenotypeItems(),result[1].getGenotypeItems());
        }
        return result;
    }

    private ArrayList<Integer> randomGenotypeCities(){
        ArrayList<Integer> result = new ArrayList<>(config.getNumOfCities());
        for(int i = 0; i<config.getNumOfCities(); i++){
            result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    private ArrayList<Integer> randomGenotypeItems(ArrayList<Integer> genotypeCities){
        ArrayList<Integer> result = new ArrayList<>(config.getNumOfCities());
        for(int i =0; i<config.getNumOfCities(); i++){
            result.add(randomItemGene(i));
        }
        return result;

    }

    private Integer randomItemGene(int cityGene){
        return (int)(Math.random()*(config.getCity(cityGene).getItems().size()+1));
    }

    public Thief randomThief(){
        ArrayList<Integer> genotypeCities = randomGenotypeCities();
        ArrayList<Integer> genotypeItems = randomGenotypeItems(genotypeCities);
        return new Thief(genotypeCities,genotypeItems,config);
    }

    public void mutateTheft(Thief thief){
        for(int i =0; i<thief.getGenotypeCities().size(); i++){
            if(Math.random()<mutationProb) {
                double prob = Math.random() * sumWage;
                if(prob < swapWage){
                    mutateSwap(thief,i);
                } else if(prob< swapWage+reverseWage){
                    mutateReverse(thief,i);
                } else{
                    mutateClosest(thief,i);
                }
            }
        }
        for(int i=0; i<thief.getGenotypeCities().size(); i++){
            if(Math.random()<mutationProb) {
                mutateItem(thief,i);
            }
        }
    }

    private void mutateClosest(Thief thief, int index) {
        int comparedIndex = (index + 1) % config.getNumOfCities();
        int closestIndex = -1;
        double closest = config.getMaxDistance();
        for(int i = 0; i<config.getNumOfCities(); i++){
            if(i!=comparedIndex){
                double distance = config.getCity(i).distanceTo(config.getCity(comparedIndex));
                if(distance<closest) closestIndex = i;
            }
        }
        swapCities(thief,index,closestIndex);

    }



    private void mutateSwap(Thief thief,int index){
        int swappedIndex = (int) (Math.random()*config.getNumOfCities());
        while(index==swappedIndex){
            swappedIndex = (int) (Math.random()*config.getNumOfCities());
        }
        swapCities(thief,index,swappedIndex);

    }

    private void swapCities(Thief thief, int index1, int index2){
        ArrayList<Integer> genotype = thief.getGenotypeCities();
        int temp = genotype.get(index1);
        genotype.set(index1,genotype.get(index2));
        genotype.set(index2,temp);
    }

    private void mutateReverse(Thief thief, int index){
        int swappedIndex = (int) (Math.random()*config.getNumOfCities());
        while(index==swappedIndex){
            swappedIndex = (int) (Math.random()*config.getNumOfCities());
        }
        int from = Math.min(index,swappedIndex);
        int to = Math.max(index,swappedIndex);
        for(int i = from; i <= to-((to-from)/2); i++){
            swapCities(thief,i,to-(i-from));
        }
    }

    private void crossItemsGenotype(ArrayList<Integer> ig1, ArrayList<Integer> ig2){
        int index1 = (int) (Math.random()*config.getNumOfCities());
        int index2 = (int) (Math.random()*config.getNumOfCities());
        while(index1==index2){
            index2 = (int) (Math.random()*config.getNumOfCities());
        }
        int from = Math.min(index1,index2);
        int to = Math.max(index1,index2);

        for(int i = from; i< to; i++){
            int temp = ig1.get(i);
            ig1.set(i,ig2.get(i));
            ig2.set(i,temp);
        }
    }

    private void mutateItem(Thief thief, int index){
        int itemGene = thief.getGenotypeItems().get(index);
        int numberOfItems = config.getCity(index).getItems().size();
        if(numberOfItems>0) {
            itemGene = (itemGene + (int) (Math.random() * 10)) % numberOfItems;
        }
    }


    private ArrayList<Integer>[] PMX(Thief thief1, Thief thief2){
        ArrayList<Integer> g1 = (ArrayList<Integer>) thief1.getGenotypeCities().clone();
        ArrayList<Integer> g2 = (ArrayList<Integer>) thief2.getGenotypeCities().clone();
        int[] child1 = new int[config.getNumOfCities()];
        int[] child2 = new int[config.getNumOfCities()];
        int[] temp1 = new int[config.getNumOfCities()];
        int[] temp2 = new int[config.getNumOfCities()];
        Arrays.fill(temp1, -1);
        Arrays.fill(temp2, -1);
        ArrayList<Integer>[] result = new ArrayList[2];

        int index1 = (int) (Math.random()*config.getNumOfCities());
        int index2 = (int) (Math.random()*config.getNumOfCities());
        while(index1==index2){
            index2 = (int) (Math.random()*config.getNumOfCities());
        }
        int from = Math.min(index1,index2);
        int to = Math.max(index1,index2);

        for(int i = from; i<= to;i++){
            child1[i] = g2.get(i);
            child2[i] = g1.get(i);
            temp1[g2.get(i)] = g1.get(i);
            temp2[g1.get(i)] = g2.get(i);
        }

        for(int i=0; i<config.getNumOfCities(); i++){
            if(i<from||i>to){
                int n1 = g1.get(i);
                int n2 = g2.get(i);
                int t1 = temp1[n1];
                int t2 = temp2[n2];

                while(t1!=-1){
                    n1 = t1;
                    t1 = temp1[t1];
                }
                while(t2!=-1){
                    n2 = t2;
                    t2 = temp2[t2];
                }
                child1[i] = n1;
                child2[i] = n2;
            }
        }
        result[0] = new ArrayList<Integer>(config.getNumOfCities());
        result[1] = new ArrayList<Integer>(config.getNumOfCities());
        for(int i : child1) result[0].add(i);
        for(int i : child2) result[1].add(i);

        return result;
    }

}
