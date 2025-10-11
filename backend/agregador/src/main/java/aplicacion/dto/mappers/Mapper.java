package aplicacion.dto.mappers;

public interface Mapper <Input, Output> {
    Output map(Input input);
}
