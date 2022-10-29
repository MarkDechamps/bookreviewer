package be.md.book_rater.book_rating;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookRatingDTO {

    private Long id;

    @Size(max = 1024)
    private String review;

    @NotNull
    private DifficultyEvaluation difficultyEvaluation;

    @NotNull
    private UUID bookRatingBook;

    @NotNull
    private Long raterBookRating;

}
