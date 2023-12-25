package com.quan.ebook.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import com.quan.ebook.EbookApplication;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.dto.BookListDto;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.repositories.BookRepos;
import com.quan.ebook.services.BookService;
import com.quan.ebook.testUtils.DataGenerator;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = { EbookApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles({ "test" })
public class BookControllerIntegrationTest {

    @Autowired
    DataGenerator dataGenerator;

    @MockBean
    BookService bookService;

    @InjectMocks
    BookRepos bookRepos;

    @LocalServerPort
    private int port;

    private final String urlPath = "/ebooks";
    private final String bookIdNotFound = "lakjsdflka3213";
    private final String bookIdExisted = "793678c2-adaf-450a-b236-cce93eb555d0";

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void givenValidGetAllBookRequest_whenRequestToGetAllBookEndpoint_thenSuccessResponseAndListOfBookReturn() {
        List<Book> books = dataGenerator.generateBookList();
        BookListDto bookListDto = BookListDto.builder().data(books).build();

        when(bookService.getAllBooks()).thenReturn(Mono.just(bookListDto));

        JsonPath jsonResponse = given()
                .when()
                .get(urlPath)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data", hasSize(equalTo(books.size())))
                .extract()
                .jsonPath();

        assertEquals(jsonResponse.getString("data[0].id"), books.get(0).getId());
        assertEquals(jsonResponse.getString("data[1].id"), books.get(1).getId());
    }

    @Test
    public void givenValidGetBookByIdRequest_whenRequestToGetBookByIdEndpoint_thenSuccessResponseAndBookDtoReturn() {
        Book book = dataGenerator.generateBook();

        when(bookService.getBookById(book.getId())).thenReturn(Mono.just(dataGenerator.convertBookToBookDto(book)));

        String urlForGetBookId = urlPath + "/" + book.getId();

        JsonPath jsonResponse = given()
                .when()
                .get(urlForGetBookId)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertEquals(jsonResponse.getString("author"), book.getAuthor());
        assertEquals(jsonResponse.getString("title"), book.getTitle());
        assertEquals(jsonResponse.getString("format"), book.getFormat().getName());
    }

    @Test
    public void givenValidGetBookByIdRequest_whenRequestToGetBookByIdEndpoint_thenFailedResponseAndNotFoundReturn() {
        String errorString = "Book not found with id: " + bookIdNotFound;
        String UrlForGetBookNotFound = urlPath + "/" + bookIdNotFound;

        when(bookService.getBookById(bookIdNotFound)).thenReturn(Mono.error(new EntityNotFoundException(errorString)));

        JsonPath jsonResponse = given()
                .when()
                .get(UrlForGetBookNotFound)
                .then()
                .assertThat()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertEquals(jsonResponse.getString("message"), errorString);
    }

    // @Test
    // public void givenValidSaveBookRequest_whenRequestToSaveBookEndpoint_thenSuccessResponseAndBookDtoReturn()
    //         throws Exception {
    //     Book book = dataGenerator.generateBook();
    //     BookDto bookDto = dataGenerator.convertBookToBookDto(book);

    //     when(bookService.saveBook(bookDto)).thenReturn(Mono.just(bookDto));

    //     // Map<String, String> requestBody = new HashMap<>();
    //     // requestBody.put("author", book.getAuthor());
    //     // requestBody.put("title", book.getTitle());
    //     // requestBody.put("format", book.getFormat().getName());

    //     Response response = given()
    //                             .contentType(ContentType.JSON)
    //                             .body(dataGenerator.convertBookDtoToString(bookDto))
    //                             .when()
    //                             .post(urlPath);

    //     System.out.println("Response Status Code: " + response.getStatusCode());
    //     System.out.println("Response Content-Type: " + response.getContentType());
    //     System.out.println("Response Body: " + response.getBody().asString());

    //     JsonPath jsonResponse = response
    //                                 .then()
    //                                 .assertThat()
    //                                 .statusCode(201)
    //                                 .contentType(ContentType.JSON)                      
    //                                 .extract()
    //                                 .jsonPath();

    //     assertEquals(jsonResponse.getString("author"), book.getAuthor());
    //     assertEquals(jsonResponse.getString("title"), book.getTitle());
    //     assertEquals(jsonResponse.getString("format"), book.getFormat().getName());
    // }

    @Test
    public void givenValidDeleteBookByIdRequest_whenRequestToDeleteBookByIdEndpoint_thenSuccessResponse() {
        when(bookService.deleteById(bookIdExisted)).thenReturn(Mono.empty());

        String urlForGetBookId = urlPath + "/" + bookIdExisted;

        given()
            .when()
            .delete(urlForGetBookId)
            .then()
            .assertThat()
            .statusCode(204);
    }

    @Test
    public void givenValidDeleteBookByIdRequest_whenRequestToDeleteBookByIdEndpoint_thenFailedResponseAndNotFoundReturn() {
        String errorString = "Book not found with id: " + bookIdNotFound;
        String UrlForGetBookNotFound = urlPath + "/" + bookIdNotFound;

        when(bookService.deleteById(bookIdNotFound)).thenReturn(Mono.error(new EntityNotFoundException(errorString)));

        JsonPath jsonResponse = given()
                .when()
                .delete(UrlForGetBookNotFound)
                .then()
                .assertThat()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertEquals(jsonResponse.getString("message"), errorString);
    }


    @Test
    public void givenValidUpdateBookByIdRequest_whenRequestToUpdateBookByIdEndpoint_thenSuccessResponse() {
        Book book = dataGenerator.generateBook();
        Book newBook = book;

        String newTitle = "new title";
        newBook.setTitle(newTitle);

        String newAuthor = "new author";
        newBook.setAuthor(newAuthor);

        when(bookService.updateBook(book.getId(), newAuthor, newTitle, null)).thenReturn(Mono.just(dataGenerator.convertBookToBookDto(newBook)));

        String urlForUpdateBookId = urlPath + "/" + book.getId();

        JsonPath jsonResponse = given()
                .contentType(ContentType.JSON)
                .param("title", newTitle)
                .param("author", newAuthor)
                .when()
                .patch(urlForUpdateBookId)
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertEquals(jsonResponse.getString("author"), newAuthor);
        assertEquals(jsonResponse.getString("title"), newTitle);
        assertEquals(jsonResponse.getString("format"), book.getFormat().getName());
    }

    @Test
    public void givenValidUpdateBookByIdRequest_whenRequestToUpdateBookByIdEndpoint_thenFailedResponseAndNotFoundReturn() {
        String errorString = "Book not found with id: " + bookIdNotFound;
        String UrlForUpdateBookNotFound = urlPath + "/" + bookIdNotFound;
        String newTitle = "new title";
        String newAuthor = "new author"; 

        when(bookService.updateBook(bookIdNotFound, newAuthor, newTitle, null)).thenReturn(Mono.error(new EntityNotFoundException(errorString)));

        JsonPath jsonResponse = given()
                .contentType(ContentType.JSON)
                .param("title", newTitle)
                .param("author", newAuthor)
                .when()
                .patch(UrlForUpdateBookNotFound)
                .then()
                .assertThat()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath();

        assertEquals(jsonResponse.getString("message"), errorString);
    }
}
