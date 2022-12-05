package ma.fourstages.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionAccount {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String account;
    private BigDecimal credit;
    private BigDecimal debit;
    @ManyToOne
    @JsonIgnore
    private Correction correction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorrectionAccount that = (CorrectionAccount) o;

        if (!date.equals(that.date)) return false;
        if (!Objects.equals(account, that.account)) return false;
        if (credit.compareTo(that.credit) != 0) return false;
        return debit.compareTo(that.debit) == 0;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (credit != null ? credit.hashCode() : 0);
        result = 31 * result + (debit != null ? debit.hashCode() : 0);
        return result;
    }
}
