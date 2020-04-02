package com.example;

import java.util.Collections;
import java.util.List;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@Timeout(value = 100)
	@Retry(maxRetries = 1, delay = 500, jitter = 100)
	@Fallback(fallbackMethod = "findAllFallback")
	@GetMapping
	public Iterable<Book> findAll() {
		return bookService.findAll();
	}

	@Counted(name = "fallbackCount", description = "How many times the fallback has been called.")
	@SimplyTimed(name = "fallbackTimer", description = "Time spent inthe fallback method", unit = MetricUnits.MILLISECONDS)
	public Iterable<Book> findAllFallback() {
		return Collections.emptyList();
	}

	@GetMapping("/year/{lower}/{higher}")
	public List<Book> findByPublicationYearBetween(@PathVariable Integer lower, @PathVariable Integer higher) {
		return bookService.findByPublicationYearBetween(lower, higher);
	}

	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Integer id) {
		try {
			bookService.deleteById(id);
		}
		catch (Exception e) {
			throw new MissingBookException();
		}
	}

	@PostMapping("/suffix/{name}/{suffix}")
	public Iterable<Book> addSuffixToMatching(@PathVariable String name, @PathVariable String suffix) {
		bookService.addSuffixToMatching(name, suffix);
		return bookService.findAll();
	}
}

