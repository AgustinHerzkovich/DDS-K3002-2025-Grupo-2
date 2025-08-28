package aplicacion.services.mappers;

public interface Mapper <Input, Output> {
    Output map(Input input);
}
