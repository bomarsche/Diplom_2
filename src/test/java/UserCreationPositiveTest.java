import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;


public class UserCreationPositiveTest {
    private User user;
    private UserClient userClient;
    private String accessToken = null;

    @Before
    public void setUp() {
        user = UserGenerator.getRandomFullData();
        userClient = new UserClient();
    }


    @Test
    @DisplayName("Регистрация пользователя")
    @Description("POST /api/auth/register → 200 OK || {success: true}")
    public void createUserTest() {
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Регистрация пользователя с уже зарегистрированным email")
    @Description("POST /api/auth/register → 403 FORBIDDEN || {message: User already exists}")
    public void createUserRecurringEmailTest() {
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));

        User recurringUser = new User(user.getEmail(), user.getName(), user.getPassword());
        ValidatableResponse createRecurringResponse = userClient.createUser(recurringUser);
        accessToken = createResponse.extract().path("accessToken");
        createRecurringResponse.assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
    }


    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.deleteUser(accessToken);
        }
    }
}
