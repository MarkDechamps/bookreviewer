package be.md.book_rater.rater;

import be.md.book_rater.util.WebUtils;
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
@RequestMapping("/raters")
public class RaterController {

    private final RaterService raterService;

    public RaterController(final RaterService raterService) {
        this.raterService = raterService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("raters", raterService.findAll());
        return "rater/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("rater") final RaterDTO raterDTO) {
        return "rater/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("rater") @Valid final RaterDTO raterDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rater/add";
        }
        raterService.create(raterDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rater.create.success"));
        return "redirect:/raters";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("rater", raterService.get(id));
        return "rater/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("rater") @Valid final RaterDTO raterDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rater/edit";
        }
        raterService.update(id, raterDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rater.update.success"));
        return "redirect:/raters";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = raterService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            raterService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("rater.delete.success"));
        }
        return "redirect:/raters";
    }

}
