package be.md.book_rater.book;

import be.md.book_rater.util.WebUtils;
import java.util.UUID;
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
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("books", bookService.findAll());
        return "book/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("book") final BookDTO bookDTO) {
        return "book/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("book") @Valid final BookDTO bookDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "book/add";
        }
        bookService.create(bookDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("book.create.success"));
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id, final Model model) {
        model.addAttribute("book", bookService.get(id));
        return "book/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final UUID id,
            @ModelAttribute("book") @Valid final BookDTO bookDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookService.update(id, bookDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("book.update.success"));
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final UUID id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = bookService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            bookService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("book.delete.success"));
        }
        return "redirect:/books";
    }

}
