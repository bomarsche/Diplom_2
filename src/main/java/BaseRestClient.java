import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRestClient {

    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api/";
    protected static final String USER_CREATE = "auth/register";
    protected static final String USER_LOGIN = "auth/login";
    protected static final String USER_PATH = "auth/user";
    protected static final String ORDER_PATH = "orders/";
    protected static final String INGREDIENTS_PATH = "ingredients";


    protected RequestSpecification getReqSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .build();
    }

    protected RequestSpecification getReqSpecAuth(String accessToken) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader("authorization", accessToken)
                .build();
    }
}
