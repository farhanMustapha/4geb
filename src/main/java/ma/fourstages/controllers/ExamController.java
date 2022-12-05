package ma.fourstages.controllers;

import lombok.RequiredArgsConstructor;
import ma.fourstages.entities.Exam;
import ma.fourstages.services.ExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @PostMapping
    public Exam save(@RequestBody Exam exam) {
        return examService.save(exam);
    }

    @GetMapping
    public List<Exam> getAll() {
        return examService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        examService.delete(id);
    }
}
