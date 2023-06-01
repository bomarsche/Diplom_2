import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseRestClient {

    @Step("Создание заказа c авторизацией")
    public ValidatableResponse createOrderWithAuth(Order order, String accessToken) {
        return given().log().all()
                .spec(getReqSpecAuth(accessToken))
                .body(order)
                .post(BaseRestClient.ORDER_PATH)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given().log().all()
                .spec(getReqSpec())
                .body(order)
                .post(BaseRestClient.ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов пользователя с авторизацией")
    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        return given().log().all()
                .spec(getReqSpecAuth(accessToken))
                .get(BaseRestClient.ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов пользователя без авторизации")
    public ValidatableResponse getOrdersWithoutAuth() {
        return given().log().all()
                .spec(getReqSpec())
                .get(BaseRestClient.ORDER_PATH)
                .then();
    }

}
