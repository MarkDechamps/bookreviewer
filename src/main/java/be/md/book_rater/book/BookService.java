package be.md.book_rater.book;

import be.md.book_rater.util.WebUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll(Sort.by("id"))
                .stream()
                .map(book -> mapToDTO(book, new BookDTO()))
                .collect(Collectors.toList());
    }

    public BookDTO get(final UUID id) {
        return bookRepository.findById(id)
                .map(book -> mapToDTO(book, new BookDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public UUID create(final BookDTO bookDTO) {
        final Book book = new Book();
        mapToEntity(bookDTO, book);
        return bookRepository.save(book).getId();
    }

    public void update(final UUID id, final BookDTO bookDTO) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(bookDTO, book);
        bookRepository.save(book);
    }

    public void delete(final UUID id) {
        bookRepository.deleteById(id);
    }

    private BookDTO mapToDTO(final Book book, final BookDTO bookDTO) {
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setGoodreadsID(book.getGoodreadsID());
        return bookDTO;
    }

    private Book mapToEntity(final BookDTO bookDTO, final Book book) {
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setGoodreadsID(bookDTO.getGoodreadsID());
        return book;
    }

    @Transactional
    public String getReferencedWarning(final UUID id) {
        final Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!book.getBookRatingBookBookRatings().isEmpty()) {
            return WebUtils.getMessage("book.bookRating.manyToOne.referenced", book.getBookRatingBookBookRatings().iterator().next().getId());
        }
        return null;
    }

}
