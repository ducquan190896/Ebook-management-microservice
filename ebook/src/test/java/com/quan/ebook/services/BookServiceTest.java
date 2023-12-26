package com.quan.ebook.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.quan.ebook.exceptions.BadResultException;
import com.quan.ebook.exceptions.EntityNotFoundException;
import com.quan.ebook.mappers.BookMapper;
import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.dto.BookListDto;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.repositories.BookRepos;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@Disabled
public class BookServiceTest {
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
        List<Book> bookList = getSampleBookList();
        when(bookRepos.getbooks()).thenReturn(bookList);
        Mono<BookListDto> saveBooks = bookService.getAllBooks();
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
        Book book = getFirstBookFromSampleList();
        BookDto bookDtoExpected = convertBookToBookDto(book);

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookMapper.mapBookToBookDto(book)).thenReturn(bookDtoExpected);

        Mono<BookDto> monoBookDto = bookService.getBookById(book.getId());
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
    public void BookService_GetBookById_ReturnBookNotFound() {
        Book book = getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.empty());

        Mono<BookDto> monoBookDto = bookService.getBookById(book.getId());
        StepVerifier
                .create(monoBookDto)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();
    }

    @Test
    public void BookService_SaveBook_ReturnMonoBookDto() {
        Book book = getFirstBookFromSampleList();
        BookDto bookDtoExpected = convertBookToBookDto(book);

        when(bookRepos.checkDuplicatedTitle(book.getTitle())).thenReturn(false);
        when(bookRepos.saveBook(any(Book.class))).thenReturn(book);
        when(bookMapper.mapBookToBookDto(any(Book.class))).thenReturn(bookDtoExpected);

        Mono<BookDto> monoBookDto = bookService.saveBook(bookDtoExpected);
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
        Book book = getFirstBookFromSampleList();
        BookDto bookDto= convertBookToBookDto(book);

        when(bookRepos.checkDuplicatedTitle(book.getTitle())).thenReturn(true);

        Mono<BookDto> monoBookDto = bookService.saveBook(bookDto);
        StepVerifier
                .create(monoBookDto)
                .expectErrorMatches(throwable -> throwable instanceof BadResultException)
                .verify();
    }

    @Test
    public void BookService_UpdateBook_ReturnMonoBookDto() {
        Book book = getFirstBookFromSampleList();
        Book updatedBook = Book.builder()
                .id(book.getId())
                .author(newAuthor)
                .title(newTitle)
                .format(newFormat)
                .build();
        BookDto bookDtoExpected = convertBookToBookDto(updatedBook);

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookService.updateNewBook(book, newAuthor, newTitle, newFormat)).thenReturn(updatedBook);
        when(bookMapper.mapBookToBookDto(any(Book.class))).thenReturn(bookDtoExpected);

        Mono<BookDto> monoBookDto = bookService.updateBook(book.getId(), newAuthor, newTitle, newFormat);
        StepVerifier
                .create(monoBookDto)
                .consumeNextWith(bookDto -> {
                    assertNotNull(bookDto);
                    assertEquals(bookDto.getAuthor(), newAuthor);
                    assertEquals(bookDto.getTitle(), newTitle);
                    assertEquals(bookDto.getFormat(), newFormat);

                })
                .verifyComplete();
    }

    @Test
    public void BookService_UpdateBook_ReturnBookNotFound() {
        Book book = getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.empty());

        Mono<BookDto> monoBookDto = bookService.updateBook(book.getId(), newAuthor, newTitle, newFormat);
        StepVerifier
                .create(monoBookDto)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();
    }

    @Test
    public void BookService_UpdateBook_ReturnTitleDuplicatedException() {
        Book book = getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookRepos.checkDuplicatedTitle(newTitle)).thenReturn(true);

        Mono<BookDto> monoBookDto = bookService.updateBook(book.getId(), newAuthor, newTitle, newFormat);;
        StepVerifier
                .create(monoBookDto)
                .expectErrorMatches(throwable -> throwable instanceof BadResultException)
                .verify();
    }

    @Test
    public void BookService_DeleteById_ReturnMonoVoid() {
        Book book = getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.ofNullable(book));

        Mono<Void> monoVoid = bookService.deleteById(book.getId());
        StepVerifier
                .create(monoVoid)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void BookService_DeleteById_ReturnBookNotFound() {
        Book book = getFirstBookFromSampleList();

        when(bookRepos.getBookById(book.getId())).thenReturn(Optional.empty());

        Mono<Void> monoVoid = bookService.deleteById(book.getId());
        StepVerifier
                .create(monoVoid)
                .expectErrorMatches(throwable -> throwable instanceof EntityNotFoundException)
                .verify();
    }

    private List<Book> getSampleBookList() {
        Book book1 = Book.builder()
                .id(UUID.randomUUID().toString())
                .author("abc")
                .title("abc_title")
                .format(FormatType.epub)
                .build();
        Book book2 = Book.builder()
                .id(UUID.randomUUID().toString())
                .author("abc2")
                .title("abc2_title")
                .format(FormatType.epub)
                .build();
        return List.of(book1, book2);
    }

    private Book getFirstBookFromSampleList() {
        return getSampleBookList().get(0);
    }

    private BookDto convertBookToBookDto(Book book) {
        return BookDto.builder()
                .author(book.getAuthor())
                .title(book.getTitle())
                .format(book.getFormat())
                .build();
    }

}
