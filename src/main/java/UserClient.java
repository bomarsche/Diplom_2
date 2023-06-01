import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class UserClient extends BaseRestClient {

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given().log().all()
                .spec(getReqSpec())
                .body(user)
                .post(BaseRestClient.USER_CREATE)
                .then()
                .log().body();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user, String accessToken) {
        return given().log().all()
                .spec(getReqSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .post(BaseRestClient.USER_LOGIN)
                .then()
                .log().body();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given().log().all()
                .spec(getReqSpecAuth(accessToken))
                .auth().oauth2(accessToken)
                .delete(BaseRestClient.USER_PATH)
                .then()
                .log().body();
    }

    @Step("Изменение пользователя с авторизацией")
    public ValidatableResponse editUserWithAuth(User user, String accessToken) {
        return given().log().all()
                .spec(getReqSpecAuth(accessToken))
                .body(user)
                .patch(BaseRestClient.USER_PATH)
                .then()
                .log().body();
    }

    @Step("Изменение пользователя без авторизации")
    public ValidatableResponse editUserWithoutAuth(User user) {
        return given().log().all()
                .spec(getReqSpec())
                .body(user)
                .patch(BaseRestClient.USER_PATH)
                .then()
                .log().body();
    }

}
