package aplicacion.domain.hechos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// MULTIMEDIA
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String url;

    public Multimedia(String url){
        this.url = url;
    }

}