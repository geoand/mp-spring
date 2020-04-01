package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.faulttolerance.Fallback;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	@Fallback(fallbackMethod = "emptyBooks")
	public Iterable<Book> findAllFailing() {
		throw new RuntimeException();
	}

	private Iterable<Book> emptyBooks() {
		return Collections.emptyList();
	}

	public List<Book> findByPublicationYearBetween(@PathVariable Integer lower, @PathVariable Integer higher) {
		return bookRepository.findByPublicationYearBetween(lower, higher);
	}

	public void deleteById(@PathVariable Integer id) {
		try {
			bookRepository.deleteById(id);
		}
		catch (Exception e) {
			throw new MissingBookException();
		}
	}

	public Iterable<Book> addSuffixToMatching(@PathVariable String name, @PathVariable String suffix) {
		bookRepository.addSuffixToMatching(name, suffix);
		return bookRepository.findAll();
	}
}
