package com.quan.ebook.config;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.quan.ebook.models.entities.Book;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.repositories.BookRepos;


@Configuration
public class DataImporter {
    
    @Bean
	public CommandLineRunner runner(BookRepos bookRepos) {
		return args -> {
			for (int i = 0; i < 50; i++) {
				bookRepos.saveBook(Book.builder()
										.id(UUID.randomUUID().toString())
										.author("abc..." + i)
										.format(FormatType.epub)
										.title("abc..." + i)
										.build());
			}
		};
	}
}
