import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientClient extends BaseRestClient {

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(getReqSpec())
                .get(BaseRestClient.INGREDIENTS_PATH);
    }

}
