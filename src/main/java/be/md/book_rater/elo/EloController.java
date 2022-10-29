package be.md.book_rater.elo;

import be.md.book_rater.rater.Rater;
import be.md.book_rater.rater.RaterRepository;
import be.md.book_rater.util.WebUtils;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/elos")
public class EloController {

    private final EloService eloService;
    private final RaterRepository raterRepository;

    public EloController(final EloService eloService, final RaterRepository raterRepository) {
        this.eloService = eloService;
        this.raterRepository = raterRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("originValues", Origin.values());
        model.addAttribute("raterElosValues", raterRepository.findAll().stream().collect(
                Collectors.toMap(Rater::getId, Rater::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("elos", eloService.findAll());
        return "elo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("elo") final EloDTO eloDTO) {
        return "elo/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("elo") @Valid final EloDTO eloDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "elo/add";
        }
        eloService.create(eloDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("elo.create.success"));
        return "redirect:/elos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("elo", eloService.get(id));
        return "elo/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("elo") @Valid final EloDTO eloDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "elo/edit";
        }
        eloService.update(id, eloDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("elo.update.success"));
        return "redirect:/elos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        eloService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("elo.delete.success"));
        return "redirect:/elos";
    }

}
