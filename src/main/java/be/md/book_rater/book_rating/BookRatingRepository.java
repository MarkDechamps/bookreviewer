package be.md.book_rater.book_rating;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRatingRepository extends JpaRepository<BookRating, Long> {
}
