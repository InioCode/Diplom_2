package client;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Map;

import static client.UrlConstants.BASE_URL;
import static client.UrlConstants.INGREDIENTS_LIST_ENDPOINT;

public class GetIngredients {
    public static Response getIngredientsList(){
       return RestAssured.given()
                .baseUri(BASE_URL)
                .get(INGREDIENTS_LIST_ENDPOINT);
    }

    public static String getIdIngredient(int indexOfIngredient){
        Response response = getIngredientsList();

        String string = response.then().extract().response().asString();

        Gson gson = new Gson();
        Map ingredientListMap = gson.fromJson(string, Map.class);
        ArrayList<Object> ingredientArray = (ArrayList<Object>) ingredientListMap.get("data");
        LinkedTreeMap ingredientSpecification = (LinkedTreeMap) ingredientArray.get(indexOfIngredient);
        return (String) ingredientSpecification.get("_id");
    }

}
