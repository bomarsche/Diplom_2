import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


public class OrderCreationPositiveTest {
    private User user;
    private UserClient userClient;
    private String accessToken = null;
    private Order order;
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private List<String> responseIngredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomFullData();
        order = new Order();
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();
        accessToken = userClient.createUser(user).extract().path("accessToken");
        responseIngredients = ingredientClient.getIngredients().path("data._id");
        order.setIngredients(responseIngredients);
    }

    @Test
    @DisplayName("Создание заказа после успешной авторизации пользователя")
    @Description("POST /api/orders → 200 OK || {success: true}")
    public void createOrderWithAuthTest() {
        ValidatableResponse createResponse = orderClient.createOrderWithAuth(order, accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации пользователя")
    @Description("POST /api/orders → 200 OK || {success: true}")
    public void createOrderWithoutAuthTest() {
        ValidatableResponse createResponse = orderClient.createOrderWithAuth(order, "");
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа c ингредиентами")
    @Description("POST /api/orders → 200 OK || {success: true}")
    public void createOrderWithIngredientsTest() {
        ValidatableResponse createResponse = orderClient.createOrderWithAuth(order, accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("order.ingredients", notNullValue());
    }

    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.deleteUser(accessToken);
        }
    }

}
