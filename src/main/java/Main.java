import java.io.IOException;

public class Main {
    private static final int populationSize = 460;
    private static final int generations = 2000;
    private static final double crossProb = 0.80;
    private static final double mutateProb = 0.01;
    private static final int tournamentSize = 2;
    private static final boolean evolveItems = false;
    private static final boolean elitism = true;
    private static final boolean onlyWalk = false;
    private static final boolean roulette = true;

    public static void main(String[] Args) throws IOException {

        System.in.read();

        FileLoader fileLoader = new FileLoader("hard_4.ttp",evolveItems);
        fileLoader.setOnlyWalk(onlyWalk);
        fileLoader.load();
        Config config = fileLoader.getConfig();
        EvolutionProcess evolutionProcess = new EvolutionProcess(populationSize,crossProb,mutateProb,generations,config, roulette, tournamentSize);
        evolutionProcess.setElitism(elitism);

        CSVCreator csvCreator = new CSVCreator(config);

        evolutionProcess.evolve();
        Thief thief = evolutionProcess.best();
        for(Item i : thief.getKnapsack()){
            System.out.println(i.getPrice() + " " + i.getWeight());
        }
        System.out.println(thief.getTotalWalkTime());
        thief.getGenotypeCities().forEach(s-> System.out.print(s+","));
        System.out.println("\nWeight: " + thief.getCurrentWeight() + "/" + config.getKnapsackCapacity());

        csvCreator.exportThiefRoute(thief);
        csvCreator.exportGenerationsChart(evolutionProcess.getBests(),evolutionProcess.getAvarges(),evolutionProcess.getWorsts());
        csvCreator.exportThiefItems(thief);


    }
}
