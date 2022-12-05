package ma.fourstages.services;

import lombok.RequiredArgsConstructor;
import ma.fourstages.entities.Exam;
import ma.fourstages.exceptions.InvalidInputException;
import ma.fourstages.repositories.ExamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;

    public Exam save(Exam exam) {
        if (exam.getName() == null || exam.getName().isEmpty())
            throw new InvalidInputException("Exam name should not be blank");
        return this.examRepository.save(exam);
    }

    public void delete(Long id) {
        if (id == null)
            throw new InvalidInputException("Exam name should not be blank");
        this.examRepository.findById(id).orElseThrow(() ->
                new InvalidInputException("Exam with id: " + id + " not found"));
        this.examRepository.deleteById(id);
    }

    public List<Exam> getAll() {
        return this.examRepository.findAll();
    }
}
