package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.common.CommonCategories;
import hcmute.lp.backend.model.dto.import_.*;
import hcmute.lp.backend.model.entity.Import;
import hcmute.lp.backend.model.entity.ImportDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImportMapper {

    ImportMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ImportMapper.class);

    ImportDto toDto(Import importEntity);

    Import toEntity(ImportRequest importRequest);

    ImportDetailDto toDetailDto(ImportDetail importDetail);

    ImportDetail toDetailEntity(ImportDetailRequest importDetailRequest);

    List<ImportDto> toDtoList(List<Import> imports);
    List<ImportDetailDto> toDetailDtoList(List<ImportDetail> importDetails);
}