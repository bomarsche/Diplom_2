import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.hamcrest.Matchers.equalTo;


@RunWith(Parameterized.class)
public class OrderCreationNegativeTest {
    private User user;
    private UserClient userClient;
    private String accessToken = null;
    private Order order;
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private List<String> ingredients;

    public OrderCreationNegativeTest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {List.of("61c0c5a71d1f82001bdaaa6dxx", "xx61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa72xx")},
                {List.of("")},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomFullData();
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        accessToken = userClient.createUser(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание заказа после успешной авторизации пользователя")
    @Description("POST /api/orders → 500 Internal Server Error || {success: false}")
    public void createOrderWithAuthTest() {
        order = new Order(ingredients);
        ValidatableResponse createResponse = orderClient.createOrderWithAuth(order, accessToken);
        createResponse.assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя")
    @Description("POST /api/orders → 500 Internal Server Error || {success: false}")
    public void createOrderWithoutAuthTest() {
        order = new Order(ingredients);
        ValidatableResponse createResponse = orderClient.createOrderWithoutAuth(order);
        createResponse.assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов после успешной авторизации пользователя")
    @Description("POST /api/orders → 400 Bad Request || {success: false}")
    public void createOrderWithoutIngredientsTest() {
        order = new Order();
        ValidatableResponse createResponse = orderClient.createOrderWithAuth(order, accessToken);
        createResponse.assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));

    }


    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.deleteUser(accessToken);
        }
    }


}
