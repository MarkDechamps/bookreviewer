package be.md.book_rater.book_rating;

import be.md.book_rater.book.Book;
import be.md.book_rater.book.BookRepository;
import be.md.book_rater.rater.Rater;
import be.md.book_rater.rater.RaterRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class BookRatingService {

    private final BookRatingRepository bookRatingRepository;
    private final BookRepository bookRepository;
    private final RaterRepository raterRepository;

    public BookRatingService(final BookRatingRepository bookRatingRepository,
            final BookRepository bookRepository, final RaterRepository raterRepository) {
        this.bookRatingRepository = bookRatingRepository;
        this.bookRepository = bookRepository;
        this.raterRepository = raterRepository;
    }

    public List<BookRatingDTO> findAll() {
        return bookRatingRepository.findAll(Sort.by("id"))
                .stream()
                .map(bookRating -> mapToDTO(bookRating, new BookRatingDTO()))
                .collect(Collectors.toList());
    }

    public BookRatingDTO get(final Long id) {
        return bookRatingRepository.findById(id)
                .map(bookRating -> mapToDTO(bookRating, new BookRatingDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final BookRatingDTO bookRatingDTO) {
        final BookRating bookRating = new BookRating();
        mapToEntity(bookRatingDTO, bookRating);
        return bookRatingRepository.save(bookRating).getId();
    }

    public void update(final Long id, final BookRatingDTO bookRatingDTO) {
        final BookRating bookRating = bookRatingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(bookRatingDTO, bookRating);
        bookRatingRepository.save(bookRating);
    }

    public void delete(final Long id) {
        bookRatingRepository.deleteById(id);
    }

    private BookRatingDTO mapToDTO(final BookRating bookRating, final BookRatingDTO bookRatingDTO) {
        bookRatingDTO.setId(bookRating.getId());
        bookRatingDTO.setReview(bookRating.getReview());
        bookRatingDTO.setDifficultyEvaluation(bookRating.getDifficultyEvaluation());
        bookRatingDTO.setBookRatingBook(bookRating.getBookRatingBook() == null ? null : bookRating.getBookRatingBook().getId());
        bookRatingDTO.setRaterBookRating(bookRating.getRaterBookRating() == null ? null : bookRating.getRaterBookRating().getId());
        return bookRatingDTO;
    }

    private BookRating mapToEntity(final BookRatingDTO bookRatingDTO, final BookRating bookRating) {
        bookRating.setReview(bookRatingDTO.getReview());
        bookRating.setDifficultyEvaluation(bookRatingDTO.getDifficultyEvaluation());
        final Book bookRatingBook = bookRatingDTO.getBookRatingBook() == null ? null : bookRepository.findById(bookRatingDTO.getBookRatingBook())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "bookRatingBook not found"));
        bookRating.setBookRatingBook(bookRatingBook);
        final Rater raterBookRating = bookRatingDTO.getRaterBookRating() == null ? null : raterRepository.findById(bookRatingDTO.getRaterBookRating())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "raterBookRating not found"));
        bookRating.setRaterBookRating(raterBookRating);
        return bookRating;
    }

}
