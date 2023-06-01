import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserCreationNegativeTest {
    private User user;
    private UserClient userClient;
    private String accessToken = null;

    public UserCreationNegativeTest(User user) {
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {UserGenerator.getRandomWithoutEmail()},
                {UserGenerator.getRandomWithoutName()},
                {UserGenerator.getRandomWithoutPassword()},
                {UserGenerator.getRandomEmpty()},
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Регистрация пользователя без одного или нескольких обязательных параметров")
    @Description("POST /api/auth/register → 403 FORBIDDEN || {message: Email, password and name are required fields}")
    public void createUserWithNotEnoughDataEmptyTest() {
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
        createResponse.assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }


}
