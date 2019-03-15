import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class ScoringTests {
    @Test
    public void testDistance(){
        City c1 = new City(0,0,0);
        City c2 = new City(1,100,0);
        City c3 = new City(2,0,100);
        City c4 = new City(3,100,100);
        City c5 = new City(4,25,75);
        Assert.assertEquals(0,c1.distanceTo(c1),0.01);
        Assert.assertEquals(100,c1.distanceTo(c2),0.01);
        Assert.assertEquals(100,c1.distanceTo(c3),0.01);
        Assert.assertEquals(141.4213562,c2.distanceTo(c3),0.01);
        Assert.assertEquals(141.4213562,c1.distanceTo(c4),0.01);
        Assert.assertEquals(79.056941,c4.distanceTo(c5),0.01);
        Assert.assertEquals(79.056941,c1.distanceTo(c5),0.01);
        Assert.assertEquals(35.35533905,c3.distanceTo(c5),0.01);
    }
    @Test
    public void testWalkDistance(){
        City c1 = new City(0,0,0);
        City c2 = new City(1,100,0);
        City c3 = new City(2,0,100);
        City c4 = new City(3,100,100);
        City c5 = new City(4,25,75);
        ArrayList<City> cities = new ArrayList<>();
        ArrayList<Integer> genotype1 = new ArrayList<>();
        ArrayList<Integer> genotype2 = new ArrayList<>();
        cities.add(c1); genotype1.add(0);   genotype2.add(1);
        cities.add(c2); genotype1.add(1);   genotype2.add(2);
        cities.add(c3); genotype1.add(2);   genotype2.add(4);
        cities.add(c4); genotype1.add(3);   genotype2.add(0);
        cities.add(c5); genotype1.add(4);   genotype2.add(3);
        Config config = new Config(0.1,1,10,5,false,cities);
        Thief thief1 = new Thief(genotype1,genotype1,config);
        Thief thief2 = new Thief(genotype2,genotype2,config);
        Thief thief3 = new Thief(thief2);
        Assert.assertEquals(499.5352382,thief1.getTotalWalkTime(),0.01);
        Assert.assertEquals(497.2549920,thief2.getTotalWalkTime(),0.01);
        Assert.assertEquals(497.2549920,thief3.getTotalWalkTime(),0.01);

    }
}
