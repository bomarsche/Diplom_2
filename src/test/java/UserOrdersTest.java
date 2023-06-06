import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


public class UserOrdersTest {
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
        orderClient.createOrderWithAuth(order, accessToken);
    }

    @Test
    @DisplayName("Получить заказы авторизованного пользователя")
    @Description("GET /api/orders → 200 OK || {success: true}")
    public void getOrdersWithAuthTest() {
        ValidatableResponse createResponse = orderClient.getOrdersWithAuth(accessToken);
        createResponse.assertThat()
                .statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получить заказы неавторизованного пользователя")
    @Description("GET /api/orders → 401 Unauthorized || {success: false}")
    public void getOrdersWithoutAuthTest() {
        ValidatableResponse createResponse = orderClient.getOrdersWithoutAuth();
        createResponse.assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void cleanUp() {
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.deleteUser(accessToken);
        }
    }

}
