package be.md.book_rater.elo;

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
@RequestMapping(value = "/api/elos", produces = MediaType.APPLICATION_JSON_VALUE)
public class EloResource {

    private final EloService eloService;

    public EloResource(final EloService eloService) {
        this.eloService = eloService;
    }

    @GetMapping
    public ResponseEntity<List<EloDTO>> getAllElos() {
        return ResponseEntity.ok(eloService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EloDTO> getElo(@PathVariable final Long id) {
        return ResponseEntity.ok(eloService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createElo(@RequestBody @Valid final EloDTO eloDTO) {
        return new ResponseEntity<>(eloService.create(eloDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateElo(@PathVariable final Long id,
        @RequestBody @Valid final EloDTO eloDTO) {
        eloService.update(id, eloDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteElo(@PathVariable final Long id) {
        eloService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
