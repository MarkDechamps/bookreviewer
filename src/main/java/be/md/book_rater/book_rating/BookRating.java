package be.md.book_rater.book_rating;

import be.md.book_rater.book.Book;
import be.md.book_rater.rater.Rater;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class BookRating {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(length = 1024)
    private String review;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyEvaluation difficultyEvaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_rating_book_id", nullable = false)
    private Book bookRatingBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rater_book_rating_id", nullable = false)
    private Rater raterBookRating;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
