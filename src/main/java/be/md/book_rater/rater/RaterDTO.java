package be.md.book_rater.rater;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RaterDTO {

    private Long id;

    @Size(max = 255)
    private String name;

}
