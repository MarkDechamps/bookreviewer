package be.md.book_rater.book_rating;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/bookRatings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookRatingResource {

    private final BookRatingService bookRatingService;

    public BookRatingResource(final BookRatingService bookRatingService) {
        this.bookRatingService = bookRatingService;
    }

    @GetMapping
    public ResponseEntity<List<BookRatingDTO>> getAllBookRatings() {
        return ResponseEntity.ok(bookRatingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRatingDTO> getBookRating(@PathVariable final Long id) {
        return ResponseEntity.ok(bookRatingService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createBookRating(
            @RequestBody @Valid final BookRatingDTO bookRatingDTO) {
        return new ResponseEntity<>(bookRatingService.create(bookRatingDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBookRating(@PathVariable final Long id,
            @RequestBody @Valid final BookRatingDTO bookRatingDTO) {
        bookRatingService.update(id, bookRatingDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBookRating(@PathVariable final Long id) {
        bookRatingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
