package ma.fourstages.services;

import lombok.RequiredArgsConstructor;
import ma.fourstages.entities.Correction;
import ma.fourstages.entities.CorrectionAccount;
import ma.fourstages.entities.Exercise;
import ma.fourstages.exceptions.InvalidInputException;
import ma.fourstages.repositories.CorrectionAccountRepository;
import ma.fourstages.repositories.CorrectionRepository;
import ma.fourstages.repositories.ExamRepository;
import ma.fourstages.repositories.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExamRepository examRepository;
    private final CorrectionRepository correctionRepository;
    private final CorrectionAccountRepository correctionAccountRepository;

    public List<Exercise> getByExamId(Long examId) {
        List<Exercise> exercises = this.exerciseRepository.findByExam_Id(examId);
        return IntStream.range(0, exercises.size())
                .mapToObj(i -> {
                    Exercise ex = exercises.get(i);
                    ex.setNumber((long) (i + 1));
                    return ex;
                }).collect(Collectors.toList());
    }

    public Exercise saveExercise(Exercise exercise, Long examId) {
        if (!examRepository.findById(examId).isPresent())
            throw new InvalidInputException("Unknown Exam id : " + examId);
        if (exercise.getQuestion() == null || exercise.getQuestion().isEmpty()) {
            throw new InvalidInputException("Exercise question should not be blank");
        }
        if (exercise.getNumber() == null) {
            throw new InvalidInputException("Exercise number should not be blank");
        }
        if (exercise.getCorrection() == null) {
            throw new InvalidInputException("Exercise correction should not be blank");
        }
        if (exercise.getCorrection().getJournal() == null || exercise.getCorrection().getJournal().isEmpty()) {
            throw new InvalidInputException("Exercise correction journal should not be blank");
        }
        if (exercise.getCorrection().getAccounts() == null || exercise.getCorrection().getAccounts().isEmpty()) {
            throw new InvalidInputException("Exercise correction account should not be blank");
        }
        exercise.setExam(examRepository.findById(examId).get());
        Correction correction = correctionRepository.save(exercise.getCorrection());
        exercise.getCorrection().getAccounts()
                .forEach(correctionAccount -> {
                    correctionAccount.setCorrection(correction);
                    correctionAccountRepository.save(correctionAccount);
                });
        exercise.setCorrection(correction);
        return exerciseRepository.save(exercise);
    }

    public boolean correct(Long examId, Long exerciseId, List<CorrectionAccount> correctionAccounts) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() ->
                new InvalidInputException("Exercise with id: " + exerciseId + " not found"));
        if (!Objects.equals(exercise.getExam().getId(), examId))
            throw new InvalidInputException("Exercise with id: " + exerciseId + " not belong to exam with id: " + examId);

        if (correctionAccounts.size() != exercise.getCorrection().getAccounts().size()) return false;

        ArrayList<CorrectionAccount> savedCorrection = new ArrayList<>(exercise.getCorrection().getAccounts());
        ArrayList<CorrectionAccount> proposedCorrection = new ArrayList<>(correctionAccounts);

        for(CorrectionAccount correction: savedCorrection) {
            if(!proposedCorrection.contains(correction)) return false;
        }
        return true;
    }
}
