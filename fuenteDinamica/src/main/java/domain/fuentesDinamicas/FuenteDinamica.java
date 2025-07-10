package domain.fuentesDinamicas;

import jakarta.persistence.Entity;
import lombok.Getter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// FUENTE DINAMICA
@Entity
public class FuenteDinamica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
}