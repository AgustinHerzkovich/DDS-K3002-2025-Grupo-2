package aplicacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageWrapper<T> {
    private List<T> content = new ArrayList<>();
    private int number = 0;
    private int size = 0;
    private int totalPages = 0;
    private long totalElements = 0;
    private boolean first = true;
    private boolean last = true;
    private int numberOfElements = 0;
}

