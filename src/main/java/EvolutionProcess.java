import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


@Getter
public class EvolutionProcess {
    private final int populationSize;
    private final double crossProb;
    private final double mutationProb;
    private final int generations;
    private final Config config;
    private final ArrayList<Double> bests;
    private final ArrayList<Double> avarges;
    private final ArrayList<Double> worsts;
    private final boolean roulette;

    private final int tournamentSize;

    @Setter private boolean elitism = false;
    private GenotypeBuilder genotypeBuilder;
    private ArrayList<Thief> currentPopulation;

    public EvolutionProcess(int populationSize, double crossProb, double mutationProb, int generations, Config config, boolean roulette, int tournamentSize) {
        this.populationSize = populationSize;
        this.crossProb = crossProb;
        this.mutationProb = mutationProb;
        this.generations = generations;
        this.config = config;
        this.roulette = roulette;
        this.tournamentSize = tournamentSize;
        bests = new ArrayList<>(generations);
        avarges = new ArrayList<>(generations);
        worsts = new ArrayList<>(generations);
    }

    public void evolve() {
        genotypeBuilder = new GenotypeBuilder(config,mutationProb,crossProb);
        int currentGeneration = 1;
        currentPopulation = genotypeBuilder.randomGeneration(populationSize);
        System.out.println(String.format("Current generation %d.\tBest:%f\tAvarge:%f\tWorst:%f",currentGeneration,best().getFitness(),avargeFitness(),worst().getFitness()));
        bests.add(best().getFitness());
        avarges.add(avargeFitness());
        worsts.add(worst().getFitness());
        while(!endCondition(currentGeneration)){
            currentPopulation = nextPopulation();
            currentGeneration++;
            System.out.println(String.format("Current generation %d.\tBest:%f\tAvarge:%f\tWorst:%f",currentGeneration,best().getFitness(),avargeFitness(),worst().getFitness()));
            bests.add(best().getFitness());
            avarges.add(avargeFitness());
            worsts.add(worst().getFitness());
        }

    }

    private Thief tournament(){
        Thief[] participates = new Thief[tournamentSize];
        int maxIndex = 0;
        double maxScore = 0;
        for(int i=0; i<tournamentSize; i++){
            if(roulette) {
                participates[i] = roulette();
            }
            else{
                participates[i] = currentPopulation.get((int) (Math.random()*populationSize));
            }
            if(i==0||participates[i].getFitness()>maxScore){
                maxIndex=i;
                maxScore=participates[i].getFitness();
            }
        }
        return participates[maxIndex];
    }

    private Thief roulette(){
        double sum = 0;
        double currentWorst = worst().getFitness();
        for(Thief t : currentPopulation){
            sum+=t.getFitness()-currentWorst;
        }
        //currentPopulation.forEach(t->System.out.print((t.getFitness()-currentWorst) + ","));
        double drawn = Math.random()*sum;
        currentPopulation.sort(Comparator.comparing(Thief::getFitness));
        for(Thief t: currentPopulation){
            drawn+= -1*(t.getFitness()-currentWorst);
            if(drawn<=0){
                return t;
            }
        }
        return null;
    }

    private ArrayList<Thief> nextPopulation() {
        ArrayList<Thief> result = new ArrayList<>(populationSize);
        ArrayList<Thief> winners = new ArrayList<>(populationSize);
        Thief best = new Thief(best());
        for(int i=0; i<populationSize; i++){
            winners.add(tournament());
        }
        for(int i=0; i<populationSize/2; i++){
            Thief[] children = genotypeBuilder.cross(winners.get(i),winners.get(i+1));
            result.add(children[0]);
            result.add(children[1]);
        }
        for(int i=0; i<populationSize; i++){
            genotypeBuilder.mutateTheft(result.get(i));
        }
        if(elitism){
            result.set((int) (Math.random()*populationSize),best);
        }
        return result;
    }

    public Thief best(){
        return Collections.max(currentPopulation, Comparator.comparingDouble(Thief::getFitness));
    }

    private Thief worst(){
        return Collections.min(currentPopulation, Comparator.comparingDouble(Thief::getFitness));
    }

    private double avargeFitness(){
        double sum = 0;
        for(int i=0; i<populationSize; i++){
            sum += currentPopulation.get(i).getFitness();
        }
        return sum/populationSize;
    }

    private boolean endCondition(int currentGen){
        return currentGen>=generations;
    }
}
