package hcmute.lp.backend.model.mapper;

import hcmute.lp.backend.model.common.Common;
import hcmute.lp.backend.model.dto.common.CommonDto;
import hcmute.lp.backend.model.dto.common.CommonRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommonMapper {

    CommonMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(CommonMapper.class);

    @Mapping(target = "id", ignore = true)
    Common toEntity(CommonRequest commonRequest);

    CommonDto toDto(Common common);

    List<CommonDto> toDtoList(List<Common> commons);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(CommonRequest commonRequest, @MappingTarget Common common);
}