package be.md.book_rater.book;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String author;

    @Size(max = 255)
    private String isbn;

    @Size(max = 255)
    private String goodreadsID;

}
