import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String accessToken = null;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomFullData();
        userClient = new UserClient();
        accessToken = userClient.createUser(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("POST /api/auth/login → 200 OK || {success: true}")
    public void loginUserTest() {
        ValidatableResponse createResponse = userClient.loginUser(user, accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Aвторизация пользователя c пустым email")
    @Description("POST /api/auth/login → 401 Unauthorized || {success: true}")
    public void loginUserWithoutEmailTest() {
        ValidatableResponse createResponse = userClient.loginUser(new User("", user.getPassword()), accessToken);
        createResponse.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Aвторизация пользователя c пустым password")
    @Description("POST /api/auth/login → 401 Unauthorized || {success: true}")
    public void loginUserWithoutPasswordTest() {
        ValidatableResponse createResponse = userClient.loginUser(new User(user.getEmail(), ""), accessToken);
        createResponse.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }


    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.deleteUser(accessToken);
        }
    }

}
