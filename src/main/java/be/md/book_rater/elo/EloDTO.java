package be.md.book_rater.elo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EloDTO {

    private Long id;

    private Origin origin;

    @NotNull
    private Long raterElos;

    @PositiveOrZero
    private int elo;
}
