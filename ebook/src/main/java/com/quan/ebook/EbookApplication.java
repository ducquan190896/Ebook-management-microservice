package com.quan.ebook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.quan.ebook.models.dto.BookDto;
import com.quan.ebook.models.enums.FormatType;
import com.quan.ebook.services.BookService;

@SpringBootApplication
public class EbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbookApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(BookService bookService) {
		return args -> {
			for (int i = 0; i < 50; i++) {
				bookService.saveBook(BookDto.builder()
										.author(i + " ...")
										.format(FormatType.epub)
										.title(i + "...." + i)
										.build())
										.subscribe();
			}
		};
	}

}
