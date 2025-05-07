package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.dto.import_.*;
import hcmute.lp.backend.model.entity.Import;
import hcmute.lp.backend.model.entity.ImportDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportMapper {

    @Mapping(target = "supplier", source = "supplier")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "importDetails", source = "importDetails")
    @Mapping(target = "status", expression = "java(mapStatusToString(importEntity.getStatus()))")
    ImportDto toDto(Import importEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "importDetails", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    Import toEntity(ImportRequest importRequest);

    @Mapping(target = "product", source = "product")
    ImportDetailDto toDetailDto(ImportDetail importDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "importOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    ImportDetail toDetailEntity(ImportDetailRequest importDetailRequest);

    List<ImportDto> toDtoList(List<Import> imports);
    List<ImportDetailDto> toDetailDtoList(List<ImportDetail> importDetails);

    // Helper method để chuyển đổi từ ImportStatus sang String
    default String mapStatusToString(Import.ImportStatus status) {
        return status != null ? status.getValue() : null;
    }
}