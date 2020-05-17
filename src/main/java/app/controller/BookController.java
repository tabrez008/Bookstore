package app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.json.JsonParser;


import app.model.Book;
import app.repo.BookRepo;
import app.services.BookService;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/rest")
public class BookController {
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookRepo repo;
	@RequestMapping(value="/list_books" , method = RequestMethod.GET, headers="Accept= application/json")
	public@ResponseBody ResponseEntity<Object> getListBooks() {
		
		List<Book> listBooks=bookService.getAllBooks();
		
		List<JSONObject> books = new ArrayList<JSONObject>();
		
	    for (Book b: listBooks) {
	        JSONObject book = new JSONObject();
	        book.put("ID", b.getBID());
	        book.put("ISBN", b.getISBN());
	        book.put("TITLE", b.getTitle());
	        book.put("AUTHOR", b.getAuthor());
	        book.put("PRICE", b.getPrice());
	        book.put("COUNT" , b.getCount());
	        
	        books.add(book);
	    }
		return new ResponseEntity<Object>(books,HttpStatus.OK);
		

	}
	
	@RequestMapping(value = "/add_books" , method = RequestMethod.POST, headers="Accept= application/json")
	public ResponseEntity<Book> addBooks(@RequestBody Book book){
		System.out.println(book.getISBN());
		
		Book availableBook =  repo.findBookByTitle(book.getTitle());
		if(availableBook != null) {
			availableBook.setCount(availableBook.getCount() + 1);
			bookService.addBook(availableBook);
			return new ResponseEntity<Book>(availableBook,HttpStatus.CREATED);
		}
		System.out.println(availableBook);
		bookService.addBook(book);		
		return new ResponseEntity<Book>(book,HttpStatus.CREATED);

	}
	
	@RequestMapping(value="/delete_book" , method = RequestMethod.DELETE , headers="Accept= application/json")
	public void deleteBookById(@RequestParam int bid) {
		
		bookService.deleteBook(bid);
		
	}
	
	@RequestMapping(value="/get_books" , method = RequestMethod.GET)
	public ResponseEntity<Book> getBooks(@RequestParam int bid) {
		Optional<Book> book = bookService.getBook(bid);
		if(book == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Book>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/get_book_by_isbn/{isbn}" , method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getBooksByIsbn(@PathVariable String isbn) {
		List<Book> book = repo.findByIsbn(isbn);
		if(book.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Book>>(book,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get_book_by_title/{title}" , method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
		List<Book> book = repo.findByTitle(title);
		if(book.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Book>>(book,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get_book_by_author/{author}" , method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
		List<Book> book = repo.findByAuthor(author);
		if(book.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Book>>(book,HttpStatus.OK);
	}
	
	@RequestMapping(value="/buy_book/{title}" , method = RequestMethod.GET)
	public ResponseEntity<Book> buyBook(@PathVariable String title) {
		Book book = repo.findBookByTitle(title);
		if(book == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		int count = book.getCount();
		if(count > 1) {
			book.setCount(count - 1);
			bookService.addBook(book);
		}
		return new ResponseEntity<Book>(book,HttpStatus.OK);
	}
	
	@RequestMapping(value="/get_media_coverage/{isbn}" , method = RequestMethod.GET)
	public ResponseEntity<HashSet<String>> getMediaCoverage(@PathVariable String isbn) {
		final String uri = "https://jsonplaceholder.typicode.com/posts";
	     
	    RestTemplate restTemplate = new RestTemplate();
	    String result = restTemplate.getForObject(uri, String.class);
	    JsonParser springParser = JsonParserFactory.getJsonParser();
	    List<Object> list = springParser.parseList(result);
	    HashSet<String> response = new HashSet<String>() ;
	    Book book = repo.findBookByIsbn(isbn);
	    if(book != null) {
	    	String title = book.getTitle();
	    	for(Object o : list) {
	    		if(o instanceof Map) {
	    			Map<String,Object> map = (Map<String,Object>) o;
	    			
	    			for (Map.Entry<String, Object> entry : map.entrySet()) {
	    				if(entry.getKey() == "title" || entry.getKey() == "body") {
	    					String value = (String) entry.getValue();
	    					System.out.println(value);
	    					if(value.contains(book.getTitle())) {
	    						response.add((String) map.get("title"));
	    					}
	    						
	    				}
	    			}

	    		}
	    	}
	    	return new ResponseEntity<HashSet<String>>(response , HttpStatus.OK);
	    }
	 
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value="/update_book" , method = RequestMethod.POST, headers="Accept= application/json")
	public ResponseEntity<Book> updateBook(@RequestBody Book book){
	
		bookService.addBook(book);		
		return new ResponseEntity<Book>(book,HttpStatus.ACCEPTED);

	}
}
