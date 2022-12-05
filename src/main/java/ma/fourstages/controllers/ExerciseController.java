package ma.fourstages.controllers;

import lombok.RequiredArgsConstructor;
import ma.fourstages.entities.CorrectionAccount;
import ma.fourstages.entities.Exercise;
import ma.fourstages.services.ExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/exams/{examId}/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping
    public List<Exercise> getByExamId(@PathVariable Long examId) {
        return this.exerciseService.getByExamId(examId);
    }

    @PostMapping
    public Exercise saveExercise(@PathVariable Long examId, @RequestBody Exercise exercise) {
        return this.exerciseService.saveExercise(exercise, examId);
    }

    @PostMapping("/{exerciseId}/correction")
    public boolean correct(@PathVariable Long examId,
                           @PathVariable Long exerciseId,
                           @RequestBody List<CorrectionAccount> correctionAccounts) {
        return this.exerciseService.correct(examId, exerciseId, correctionAccounts);
    }
}
