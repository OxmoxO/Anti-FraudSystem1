package antifraud.model.mapper;

public interface ConverterMapper<E, D> extends ToDtoMapper<E, D>,
                                               ToEntityMapper<D, E> {

}