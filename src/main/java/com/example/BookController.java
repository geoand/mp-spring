package com.example;

import java.util.List;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

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

	@Counted(name = "findAllCount", description = "How many times all the books have been fetched.")
	@Timed(name = "findAllTimer", description = "A measure of how long it takes to retrieve all books.", unit = MetricUnits.MILLISECONDS)
	@GetMapping
	public Iterable<Book> findAll() {
		return bookService.findAll();
	}

	@GetMapping("/failing")
	public Iterable<Book> findAllFailing() {
		return bookService.findAllFailing();
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
