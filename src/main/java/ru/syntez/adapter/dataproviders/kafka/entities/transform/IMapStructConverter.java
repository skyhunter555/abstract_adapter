package ru.syntez.adapter.dataproviders.kafka.entities.transform;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.syntez.adapter.dataproviders.kafka.entities.OutputSampleDocument;
import ru.syntez.adapter.entrypoints.http.entities.SampleDocument;

@Mapper(componentModel = "spring")
public interface IMapStructConverter {

    @Mappings({
            @Mapping(source="docId", target="documentId"),
            @Mapping(source="docNote", target="documentDescription")
    })
    OutputSampleDocument convert(SampleDocument sampleDocument);

}
