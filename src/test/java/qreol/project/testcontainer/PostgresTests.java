package qreol.project.testcontainer;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import qreol.project.testcontainer.model.Post;
import qreol.project.testcontainer.repository.PostRepository;
import qreol.project.testcontainer.web.dto.PostDto;

import java.util.List;

@Testcontainers
@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostgresTests {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer(
            "postgres:15.1-alpine"
    );

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        postRepository.deleteAll();
    }

    @Test
    void emptyGetAllTest() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        ".",
                        Matchers.empty()
                );
    }

    @Test
    void notEmptyGetAllTest() {
        List<Post> posts = List.of(
                new Post("First title", "First text"),
                new Post("Second title", "Second text")
        );
        postRepository.saveAll(posts);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        ".",
                        Matchers.hasSize(posts.size())
                );
    }

    @Test
    void emptyGetByIdTest() {
        long id = 1L;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts/{id}", id)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "message",
                        Matchers.equalTo("Post not found.")
                );
    }

    @Test
    void notEmptyGetByIdTest() {
        Post post = new Post("First title", "First text");
        postRepository.save(post);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/posts/{id}", post.getId())
                .then()
                .body(
                        "text",
                        Matchers.equalTo(post.getText())
                );
    }

    @Test
    void createTest() {
        PostDto dto = new PostDto("First title", "First text");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/v1/posts")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(
                        "id",
                        Matchers.notNullValue()
                )
                .body(
                        "viewsAmount",
                        Matchers.equalTo(0)
                );
    }

    @Test
    void deleteTest() {
        long id = 1l;

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/v1/posts/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
