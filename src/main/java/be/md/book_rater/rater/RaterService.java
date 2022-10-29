package be.md.book_rater.rater;

import be.md.book_rater.util.WebUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class RaterService {

    private final RaterRepository raterRepository;

    public RaterService(final RaterRepository raterRepository) {
        this.raterRepository = raterRepository;
    }

    public List<RaterDTO> findAll() {
        return raterRepository.findAll(Sort.by("id"))
                .stream()
                .map(rater -> mapToDTO(rater, new RaterDTO()))
                .collect(Collectors.toList());
    }

    public RaterDTO get(final Long id) {
        return raterRepository.findById(id)
                .map(rater -> mapToDTO(rater, new RaterDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final RaterDTO raterDTO) {
        final Rater rater = new Rater();
        mapToEntity(raterDTO, rater);
        return raterRepository.save(rater).getId();
    }

    public void update(final Long id, final RaterDTO raterDTO) {
        final Rater rater = raterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(raterDTO, rater);
        raterRepository.save(rater);
    }

    public void delete(final Long id) {
        raterRepository.deleteById(id);
    }

    private RaterDTO mapToDTO(final Rater rater, final RaterDTO raterDTO) {
        raterDTO.setId(rater.getId());
        raterDTO.setName(rater.getName());
        return raterDTO;
    }

    private Rater mapToEntity(final RaterDTO raterDTO, final Rater rater) {
        rater.setName(raterDTO.getName());
        return rater;
    }

    @Transactional
    public String getReferencedWarning(final Long id) {
        final Rater rater = raterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!rater.getRaterElosElos().isEmpty()) {
            return WebUtils.getMessage("rater.elo.oneToMany.referenced", rater.getRaterElosElos().iterator().next().getId());
        } else if (!rater.getRaterBookRatingBookRatings().isEmpty()) {
            return WebUtils.getMessage("rater.bookRating.manyToOne.referenced", rater.getRaterBookRatingBookRatings().iterator().next().getId());
        }
        return null;
    }

}
