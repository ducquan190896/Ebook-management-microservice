package com.quan.ebook.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.services.BookService;

@Configuration
public class DataImporter {
    
    @Bean
	public CommandLineRunner runner(BookService bookService) {
		return args -> {
			for (int i = 0; i < 50; i++) {
				bookService.saveBook(BookDto.builder()
										.author("abc..." + i)
										.format(FormatType.epub)
										.title("abc..." + i)
										.build())
										.subscribe();
			}
		};
	}
}
