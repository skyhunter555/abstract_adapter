package ru.syntez.adapter.core.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.syntez.adapter.core.entities.IMessagePayload;

@Mapper(componentModel = "spring")
public interface IMapStructConverter {

    //@Mappings({
    //        @Mapping(source="docId", target="documentId"),
    //        @Mapping(source="docNote", target="documentDescription")
    //})
    //Object convert(Object sampleDocument);

}
