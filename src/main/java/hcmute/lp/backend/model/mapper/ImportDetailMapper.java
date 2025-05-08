package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.import_.ImportDetailDto;
import hcmute.lp.backend.model.dto.import_.ImportDetailRequest;
import hcmute.lp.backend.model.entity.ImportDetail;
import hcmute.lp.backend.model.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ProductMapper.class})
public interface ImportDetailMapper {

    ImportDetailMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ImportDetailMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "importOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    ImportDetail toEntity(ImportDetailRequest importDetailRequest);

    @Mapping(target = "product", source = "product")
    ImportDetailDto toDto(ImportDetail importDetail);

    List<ImportDetailDto> toDtoList(List<ImportDetail> importDetails);
}