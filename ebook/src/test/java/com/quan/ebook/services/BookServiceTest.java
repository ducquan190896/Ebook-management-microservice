package com.quan.ebook.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.quan.ebook.exceptions.BadResultException;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.mappers.BookMapper;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.models.request.BookRequest;
import com.quan.ebook.models.response.BookResponse;
import com.quan.ebook.models.response.BookListResponse;
import com.quan.ebook.repositories.BookRepos;
import com.quan.ebook.testUtils.DataGenerator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
// @Disabled
public class BookServiceTest {
    @Autowired
    DataGenerator dataGenerator;

    @Mock(lenient = true)
    BookRepos bookRepos;

    @Mock(lenient = true)
    BookMapper bookMapper;

    @InjectMocks
    BookService bookService;

    private final String newAuthor = "new author";
    private final String newTitle = "new title";
    private final FormatType newFormat = FormatType.pdf;

    @Test
    public void BookService_GetBooks_ReturnMonoBooks() {
        List<Book> bookList = dataGenerator.generateBookList();
        when(bookRepos.getbooks()).thenReturn(bookList);
        Mono<BookListResponse> saveBooks = bookService.getAllBooks();
        StepVerifier
                .create(saveBooks)
                .consumeNextWith(newBookList -> {
                    assertEquals(newBookList.getData(), bookList);
                    assertEquals(newBookList.getData().size(), bookList.size());
                })
                .verifyComplete();
    }

    @Test
    public void BookService_GetBookById_ReturnMonoBookDto() {
        Book book = dataGenerator.getFirstBookFromSampleList();
        BookResponse bookDtoExpected = dataGenerator.convertBookToBookResponse(book);

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookMapper.mapBookToBookResponse(book)).thenReturn(bookDtoExpected);

        Mono<BookResponse> monoBookResponse = bookService.getBookById(book.getId());
        StepVerifier
                .create(monoBookResponse)
                .consumeNextWith(bookResponse -> {
                    assertEquals(bookResponse.getAuthor(), book.getAuthor());
                    assertEquals(bookResponse.getTitle(), book.getTitle());
                    assertEquals(bookResponse.getFormat(), book.getFormat());
                })
                .verifyComplete();
    }

    @Test
    public void BookService_GetBookById_ReturnBookNotFound() {
        Book book = dataGenerator.getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.empty());

        Mono<BookResponse> monoBookResponse = bookService.getBookById(book.getId());
        StepVerifier
                .create(monoBookResponse)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();
    }

    @Test
    public void BookService_SaveBook_ReturnMonoBookDto() {
        Book book = dataGenerator.getFirstBookFromSampleList();
        BookResponse bookResponse = dataGenerator.convertBookToBookResponse(book);
        BookRequest bookRequest= dataGenerator.convertBookToBookRequest(book);

        when(bookRepos.checkDuplicatedTitle(book.getTitle(), book.getAuthor())).thenReturn(false);
        when(bookRepos.saveBook(any(Book.class))).thenReturn(book);
        when(bookMapper.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        Mono<BookResponse> monoBookDto = bookService.saveBook(bookRequest);
        StepVerifier
                .create(monoBookDto)
                .consumeNextWith(bookDto -> {
                    assertEquals(bookDto.getAuthor(), book.getAuthor());
                    assertEquals(bookDto.getTitle(), book.getTitle());
                    assertEquals(bookDto.getFormat(), book.getFormat());
                })
                .verifyComplete();
    }

     @Test
    public void BookService_SaveBook_ReturnTitleDuplicatedException() {
        Book book = dataGenerator.getFirstBookFromSampleList();
        BookRequest bookRequest= dataGenerator.convertBookToBookRequest(book);

        when(bookRepos.checkDuplicatedTitle(book.getTitle(), book.getAuthor())).thenReturn(true);

        Mono<BookResponse> monoBookDto = bookService.saveBook(bookRequest);
        StepVerifier
                .create(monoBookDto)
                .expectErrorMatches(throwable -> throwable instanceof BadResultException)
                .verify();
    }

    @Test
    public void BookService_UpdateBook_ReturnMonoBookDto() {
        Book book = dataGenerator.getFirstBookFromSampleList();
        Book updatedBook = Book.builder()
                .id(book.getId())
                .author(newAuthor)
                .title(newTitle)
                .format(newFormat)
                .build();
        BookResponse bookDtoExpected = dataGenerator.convertBookToBookResponse(updatedBook);

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookService.updateNewBook(book, newAuthor, newTitle, newFormat)).thenReturn(updatedBook);
        when(bookMapper.mapBookToBookResponse(any(Book.class))).thenReturn(bookDtoExpected);

        Mono<BookResponse> monoBookResponse = bookService.updateBook(book.getId(), newAuthor, newTitle, newFormat.getName());
        StepVerifier
                .create(monoBookResponse)
                .consumeNextWith(bookResponse -> {
                    assertNotNull(bookResponse);
                    assertEquals(bookResponse.getAuthor(), newAuthor);
                    assertEquals(bookResponse.getTitle(), newTitle);
                    assertEquals(bookResponse.getFormat(), newFormat);

                })
                .verifyComplete();
    }

    @Test
    public void BookService_UpdateBook_ReturnBookNotFound() {
        Book book = dataGenerator.getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.empty());

        Mono<BookResponse> monoBookResponse = bookService.updateBook(book.getId(), newAuthor, newTitle, newFormat.getName());
        StepVerifier
                .create(monoBookResponse)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();
    }

    @Test
    public void BookService_UpdateBook_ReturnTitleDuplicatedException() {
        Book book = dataGenerator.getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookRepos.checkDuplicatedTitle(newTitle, newAuthor)).thenReturn(true);

        Mono<BookResponse> monoBookResponse = bookService.updateBook(book.getId(), newAuthor, newTitle, newFormat.getName());;
        StepVerifier
                .create(monoBookResponse)
                .expectErrorMatches(throwable -> throwable instanceof BadResultException)
                .verify();
    }

    @Test
    public void BookService_DeleteById_ReturnMonoVoid() {
        Book book = dataGenerator.getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));

        Mono<Void> monoVoid = bookService.deleteById(book.getId());
        StepVerifier
                .create(monoVoid)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void BookService_DeleteById_ReturnBookNotFound() {
        Book book = dataGenerator.getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.empty());

        Mono<Void> monoVoid = bookService.deleteById(book.getId());
        StepVerifier
                .create(monoVoid)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();
    }
}
