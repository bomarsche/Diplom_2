import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class UserEditTest {
    private User user;
    private UserClient userClient;
    private String accessToken = null;

    @Before
    public void setUp(){
        user = UserGenerator.getRandomFullData();
        userClient = new UserClient();
        accessToken = userClient.createUser(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Успешное изменение логина пользователя")
    @Description("POST /api/auth/ → 200 OK || {success: true}")
    public void editUserEmailWithAuthTest() {
        user.setEmail("test" + user.getEmail());
        ValidatableResponse createResponse = userClient.editUserWithAuth(user, accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Успешное изменение пароля пользователя")
    @Description("POST /api/auth/ → 200 OK || {success: true}")
    public void editUserPasswordWithAuthTest() {
        user.setPassword("test" + user.getPassword());
        ValidatableResponse createResponse = userClient.editUserWithAuth(user, accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Успешное изменение имени пользователя")
    @Description("POST /api/auth/ → 200 OK || {success: true}")
    public void editUserNameWithAuthTest() {
        user.setName("test" + user.getName());
        ValidatableResponse createResponse = userClient.editUserWithAuth(user, accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("POST /api/auth/ → 401 Unauthorized || {message: You should be authorised}")
    public void editUserWithoutAuthTest() {
        user.setEmail("test" + user.getEmail());
        ValidatableResponse createResponse = userClient.editUserWithoutAuth(user);
        createResponse.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }


    @After
    public void cleanUp(){
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.deleteUser(accessToken);
        }
    }

}
