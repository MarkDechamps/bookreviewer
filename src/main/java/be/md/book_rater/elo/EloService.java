package be.md.book_rater.elo;

import be.md.book_rater.rater.Rater;
import be.md.book_rater.rater.RaterRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class EloService {

    private final EloRepository eloRepository;
    private final RaterRepository raterRepository;

    public EloService(final EloRepository eloRepository, final RaterRepository raterRepository) {
        this.eloRepository = eloRepository;
        this.raterRepository = raterRepository;
    }

    public List<EloDTO> findAll() {
        return eloRepository.findAll(Sort.by("id")).stream().map(elo -> mapToDTO(elo, new EloDTO()))
            .collect(Collectors.toList());
    }

    public EloDTO get(final Long id) {
        return eloRepository.findById(id).map(elo -> mapToDTO(elo, new EloDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final EloDTO eloDTO) {
        final Elo elo = new Elo();
        mapToEntity(eloDTO, elo);
        return eloRepository.save(elo).getId();
    }

    public void update(final Long id, final EloDTO eloDTO) {
        final Elo elo = eloRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(eloDTO, elo);
        eloRepository.save(elo);
    }

    public void delete(final Long id) {
        eloRepository.deleteById(id);
    }

    private EloDTO mapToDTO(final Elo elo, final EloDTO eloDTO) {
        eloDTO.setId(elo.getId());
        eloDTO.setOrigin(elo.getOrigin());
        eloDTO.setRaterElos(elo.getRaterElos() == null ? null : elo.getRaterElos().getId());
        eloDTO.setElo(elo.getElo());
        return eloDTO;
    }

    private Elo mapToEntity(final EloDTO eloDTO, final Elo elo) {
        elo.setOrigin(eloDTO.getOrigin());
        final Rater raterElos = eloDTO.getRaterElos() == null ? null
            : raterRepository.findById(eloDTO.getRaterElos()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "raterElos not found"));
        elo.setRaterElos(raterElos);
        elo.setElo(eloDTO.getElo());
        return elo;
    }

}
