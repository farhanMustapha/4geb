package ma.fourstages.repositories;

import ma.fourstages.entities.Correction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrectionRepository extends JpaRepository<Correction, Long> {
}
