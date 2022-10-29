package be.md.book_rater.book_rating;

import be.md.book_rater.book.Book;
import be.md.book_rater.book.BookRepository;
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
@RequestMapping("/bookRatings")
public class BookRatingController {

    private final BookRatingService bookRatingService;
    private final BookRepository bookRepository;
    private final RaterRepository raterRepository;

    public BookRatingController(final BookRatingService bookRatingService,
            final BookRepository bookRepository, final RaterRepository raterRepository) {
        this.bookRatingService = bookRatingService;
        this.bookRepository = bookRepository;
        this.raterRepository = raterRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("difficultyEvaluationValues", DifficultyEvaluation.values());
        model.addAttribute("bookRatingBookValues", bookRepository.findAll().stream().collect(
                Collectors.toMap(Book::getId, Book::getTitle)));
        model.addAttribute("raterBookRatingValues", raterRepository.findAll().stream().collect(
                Collectors.toMap(Rater::getId, Rater::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("bookRatings", bookRatingService.findAll());
        return "bookRating/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("bookRating") final BookRatingDTO bookRatingDTO) {
        return "bookRating/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("bookRating") @Valid final BookRatingDTO bookRatingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "bookRating/add";
        }
        bookRatingService.create(bookRatingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("bookRating.create.success"));
        return "redirect:/bookRatings";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("bookRating", bookRatingService.get(id));
        return "bookRating/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("bookRating") @Valid final BookRatingDTO bookRatingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "bookRating/edit";
        }
        bookRatingService.update(id, bookRatingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("bookRating.update.success"));
        return "redirect:/bookRatings";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        bookRatingService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("bookRating.delete.success"));
        return "redirect:/bookRatings";
    }

}
