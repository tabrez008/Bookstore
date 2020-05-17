package app.services;

import java.util.List;
import java.util.Optional;

import app.model.Book;


public interface BookService {
	public void addBook(Book book);

	public Optional<Book> getBook(int bookId);

	public Book updateBook(int BookId,Book book);

	public void deleteBook(int bookId);

	public List<Book> getAllBooks();
	


}
