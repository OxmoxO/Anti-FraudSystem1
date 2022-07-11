package antifraud.model.mapper;

public interface ToDtoMapper<E, D> {

    D toDto(E entity);
}
