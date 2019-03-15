import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {
    private final int id;
    private final int weight;
    private final int price;
    private final int cityId;

    public double getScore(){
        return (double)price/weight;
    }
}
