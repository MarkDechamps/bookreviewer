package be.md.book_rater.rater;

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
@RequestMapping(value = "/api/raters", produces = MediaType.APPLICATION_JSON_VALUE)
public class RaterResource {

    private final RaterService raterService;

    public RaterResource(final RaterService raterService) {
        this.raterService = raterService;
    }

    @GetMapping
    public ResponseEntity<List<RaterDTO>> getAllRaters() {
        return ResponseEntity.ok(raterService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaterDTO> getRater(@PathVariable final Long id) {
        return ResponseEntity.ok(raterService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRater(@RequestBody @Valid final RaterDTO raterDTO) {
        return new ResponseEntity<>(raterService.create(raterDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRater(@PathVariable final Long id,
            @RequestBody @Valid final RaterDTO raterDTO) {
        raterService.update(id, raterDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRater(@PathVariable final Long id) {
        raterService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
