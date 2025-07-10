package domain.fuentesDinamicas;

import jakarta.persistence.Entity;
import lombok.Getter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

// FUENTE DINAMICA
@Entity
@NoArgsConstructor
public class FuenteDinamica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
}