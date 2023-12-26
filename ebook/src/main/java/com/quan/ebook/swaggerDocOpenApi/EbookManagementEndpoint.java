package com.quan.ebook.swaggerDocOpenApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.quan.ebook.exceptions.ErrorResponse;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.dto.BookListDto;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@Tag(name = "Ebook management endpoint", description = "The endpoints for managing ebook")
@RestController
@RequestMapping("/ebooks")
public interface EbookManagementEndpoint {

        @Operation(summary = "Get all books", description = "the endpoint to provide all books")
        @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The list of books", content = @Content(schema = @Schema(implementation = BookListDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public Mono<ResponseEntity<BookListDto>> findAll();

        @Operation(summary = "Get book by book Id", description = "the endpoint to provide the book by book Id")
        @GetMapping(value = "/{ebook_id}")
        @Parameters(@Parameter(name = "ebook_id", required = true, in = ParameterIn.PATH, description = "Id of the book", schema = @Schema(implementation = String.class)))
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The book", content = @Content(schema = @Schema(implementation = BookDto.class))),
                        @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public Mono<ResponseEntity<BookDto>> findById(@PathVariable(value = "ebook_id") String ebook_id);

        @Operation(summary = "Create a book", description = "the endpoint to create the book")
        @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "The book is created", content = @Content(schema = @Schema(implementation = BookDto.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public Mono<ResponseEntity<BookDto>> save(@RequestBody  BookDto book);

        @Operation(summary = "Update a book", description = "the endpoint to update the book")
        @PatchMapping(value = "/{ebook_id}")
        @Parameters({
                        @Parameter(name = "ebook_id", required = true, in = ParameterIn.PATH, description = "Id of the book", schema = @Schema(implementation = String.class)),
                        @Parameter(name = "author", required = false, in = ParameterIn.QUERY, description = "Author of the book", schema = @Schema(implementation = String.class)),
                        @Parameter(name = "title", required = false, in = ParameterIn.QUERY, description = "Title of the book", schema = @Schema(implementation = String.class)),
                        @Parameter(name = "format", required = false, in = ParameterIn.QUERY, description = "Format type of the book including (mobi, epub, pdf, azw, txt)", schema = @Schema(type = "string", allowableValues = {
                                        "epub", "txt", "mobi", "azw", "pdf" }))
        })
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "The book is created", content = @Content(schema = @Schema(implementation = Void.class))),
                        @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public Mono<ResponseEntity<BookDto>> update(@PathVariable(value = "ebook_id") String ebook_id,
                        @RequestParam(required = false, value = "author") String author,
                        @RequestParam(required = false, value = "title") String title,
                        @RequestParam(required = false, value = "format") FormatType format);

        @Operation(summary = "Delete book by book Id", description = "the endpoint to delete the book by book Id")
        @DeleteMapping(value = "/{ebook_id}")
        @Parameters(@Parameter(name = "ebook_id", required = true, in = ParameterIn.PATH, description = "Id of the book", schema = @Schema(implementation = String.class)))
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "The book", content = @Content(schema = @Schema(implementation = Void.class))),
                        @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public Mono<ResponseEntity<HttpStatus>> deleteById(@PathVariable(value = "ebook_id") String ebook_id);
}
