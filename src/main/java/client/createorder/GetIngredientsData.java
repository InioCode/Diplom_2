package client.createorder;

import java.util.ArrayList;
import java.util.List;

public class GetIngredientsData {
    private String success;
    private List<IngredientsData> Data;

    public GetIngredientsData() {
    }

    public GetIngredientsData(String success, ArrayList<IngredientsData> data) {
        this.success = success;
        Data = data;
    }

    public String getSuccess() {
        return success;
    }

    public List<IngredientsData> getData() {
        return Data;
    }
}
