package com.quan.ebook.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.*;
import com.quan.ebook.EbookApplication;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.services.BookService;
import com.quan.ebook.testUtils.DataGenerator;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import reactor.core.publisher.Mono;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest(classes = { EbookApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles({ "test" })
public class BookControllerIntegrationTest {

    @Autowired
    DataGenerator dataGenerator;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private final String urlPath = "/ebooks";
    private final String bookIdNotFound = "lakjsdflka3213";

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @AfterEach
    public void reset() {
        WireMock.reset();
    }

    @Test
    public void givenValidGetAllBookRequest_whenRequestToGetAllBookEndpoint_thenSuccessResponseAndListOfBookReturn() {
        // given
        WireMock.stubFor(WireMock.request("GET", WireMock.urlEqualTo(urlPath))
                .willReturn(
                        WireMock.aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBodyFile("get_all_books")));

        // when
        JsonPath jsonResponse = given()
                .when()
                .get(urlPath)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        WireMock.verify(WireMock.exactly(1), WireMock.getRequestedFor(WireMock.urlEqualTo(urlPath)));

        // then
        assertEquals(jsonResponse.getString("data[0].id"), "6e8d83c0-f47a-4cc4-a33e-b86440a8ec0d");
        assertEquals(jsonResponse.getString("data[1].id"), "54012831-5991-4dcc-a3a0-540c2aa2f1f5");
    }
}
