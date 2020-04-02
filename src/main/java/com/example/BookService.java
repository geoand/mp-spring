package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	void doDelay() {
		int delayTime;
		try {
			delayTime = (int) (Math.random() * 200);
			System.out.println("** Waiting " + delayTime + "ms **");
			TimeUnit.MILLISECONDS.sleep(delayTime);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	public Iterable<Book> findAll() {
		doDelay();
		return bookRepository.findAll();
	}

	public List<Book> findByPublicationYearBetween(@PathVariable Integer lower, @PathVariable Integer higher) {
		return bookRepository.findByPublicationYearBetween(lower, higher);
	}

	public void deleteById(@PathVariable Integer id) {
		try {
			bookRepository.deleteById(id);
		} catch (Exception e) {
			throw new MissingBookException();
		}
	}

	public Iterable<Book> addSuffixToMatching(@PathVariable String name, @PathVariable String suffix) {
		bookRepository.addSuffixToMatching(name, suffix);
		return bookRepository.findAll();
	}
}

