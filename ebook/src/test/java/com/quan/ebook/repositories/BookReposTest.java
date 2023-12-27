package com.quan.ebook.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookReposTest {
    @InjectMocks
    BookRepos bookRepos;

    private final String newAuthor = "new author";
    private final String newTitle = "new title";
    private final FormatType newFormat = FormatType.pdf;
    private final String NotFoundBookId = "lkajdk11111";

    @BeforeAll
    public void setUp() {
        Book book1 = Book.builder()
                .id(GenerateUUID())
                .author("abc")
                .title("abc_title")
                .format(FormatType.epub)
                .build();
        Book book2 = Book.builder()
                .id(GenerateUUID())
                .author("abc2")
                .title("abc2_title")
                .format(FormatType.epub)
                .build();
        bookRepos.saveBook(book1);
        bookRepos.saveBook(book2);
    }

    @Test
    public void BookRepos_GetBooks_ReturnBooks() {
        List<Book> books = bookRepos.getbooks();

        Assertions.assertThat(books).isNotNull();
        Assertions.assertThat(books.size()).isEqualTo(2);
    }

    @Test
    public void BookRepos_GetBookById_ReturnBook() {
        Book book = getFirstBookFromMockRepository();

        Book savedBook = bookRepos.getBookById(book.getId()).get();

        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getId()).isEqualTo(book.getId());
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void BookRepos_GetBookByTitle_ReturnBook() {
        Book book = getFirstBookFromMockRepository();

        Book savedBook = bookRepos.getBookByTitle(book.getTitle()).get();

        Assertions.assertThat(savedBook).isNotNull();
        Assertions.assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void BookRepos_CheckDuplicatedTitleAndAuthor_ReturnDuplicatedTitleAndAuthor() {
        boolean isDuplicated = bookRepos.checkDuplicatedTitle(newTitle, newAuthor);

        Assertions.assertThat(isDuplicated).isEqualTo(false);
    }

    @Test
    public void BookRepos_GetBookById_ReturnBookNotFound() {
        Optional<Book> savedBook = bookRepos.getBookById(NotFoundBookId);

        Assertions.assertThat(savedBook.isPresent()).isEqualTo(false);
    }

    @Test
    public void BookRepos_SaveBook_ReturnUpdatedBook() {
        Book book = getFirstBookFromMockRepository();

        // update book
        book.setAuthor(newAuthor);
        book.setTitle(newTitle);
        book.setFormat(newFormat);
        Book updatedBook = bookRepos.saveBook(book);

        Assertions.assertThat(updatedBook).isNotNull();
        Assertions.assertThat(updatedBook.getId()).isEqualTo(book.getId());
        Assertions.assertThat(updatedBook.getAuthor()).isEqualTo(newAuthor);
        Assertions.assertThat(updatedBook.getTitle()).isEqualTo(newTitle);
        Assertions.assertThat(updatedBook.getFormat()).isEqualTo(newFormat);
    }

    @Test
    public void BookRepos_SaveBook_ReturnNewBook() {
        Book book = getFirstBookFromMockRepository();

        Book newBook = bookRepos.saveBook(book);

        Assertions.assertThat(newBook).isNotNull();
        Assertions.assertThat(newBook.getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(newBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(newBook.getFormat()).isEqualTo(book.getFormat());
    }

    @Test
    public void BookRepos_DeleteBook_ReturnBookIsEmpty() {
        Book book = createSampleBook();
        bookRepos.deleteBook(book);
        Optional<Book> bookOptional = bookRepos.getBookById(book.getId());

        Assertions.assertThat(bookOptional.isPresent()).isEqualTo(false);
    }

    private Book getFirstBookFromMockRepository() {
        return bookRepos.getbooks().get(0);
    }

    private Book createSampleBook() {
        Book book = Book.builder()
                .id(GenerateUUID())
                .author("abc3")
                .title("abc3_title")
                .format(FormatType.epub)
                .build();
        return bookRepos.saveBook(book);
    }

    private String GenerateUUID() {
        return UUID.randomUUID().toString();
    }
}
