import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientClient extends BaseRestClient {

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given().log().all()
                .spec(getReqSpec())
                .get(BaseRestClient.INGREDIENTS_PATH);
    }

}
