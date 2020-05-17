package app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Book;
import app.repo.BookRepo;
@Service
public class BookTask implements BookService {
	@Autowired
	private BookRepo bookrepo;
	
	@Override
	public void addBook(Book book) {
		// TODO Auto-generated method stub
		bookrepo.save(book);
	}

	@Override
	public Optional<Book> getBook(int bookId) {
		// TODO Auto-generated method stub
		return bookrepo.findById(bookId);
	}

	@Override
	public Book updateBook(int BookId, Book book) {
		// TODO Auto-generated method stub
		return bookrepo.save(book);
	}

	@Override
	public void deleteBook(int bookId) {
		// TODO Auto-generated method stub
		bookrepo.deleteById(bookId);
	}

	@Override
	public List<Book> getAllBooks() {
		// TODO Auto-generated method stub
		List<Book> books = bookrepo.findAll();
		
		return books;
	}


}
