package app.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import app.model.Book;

public interface BookRepo extends JpaRepository<Book, Integer>{
	@Query("SELECT e FROM Book e WHERE e.ISBN LIKE %?1%")
	public List<Book> findByIsbn(String place);
	
	@Query("SELECT e FROM Book e WHERE e.title LIKE %?1%")
	public List<Book> findByTitle(String place);
	
	@Query("SELECT e FROM Book e WHERE e.author LIKE %?1%")
	public List<Book> findByAuthor(String place);
	
	@Query("FROM Book WHERE title = ?1")
	public Book findBookByTitle(String book);

	@Query("FROM Book WHERE ISBN = ?1")
	public Book findBookByIsbn(String isbn);
}
