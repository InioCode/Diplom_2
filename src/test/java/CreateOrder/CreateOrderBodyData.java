package CreateOrder;

import java.util.List;

public class CreateOrderBodyData {

    private List<String> ingredients;

    public CreateOrderBodyData(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public CreateOrderBodyData() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
