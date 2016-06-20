package apolo.api.helper;

import apolo.api.apimodel.base.BaseAPIModel;
import apolo.data.model.base.BaseEntity;

import java.util.List;

public interface ApoloHelper<Entity extends BaseEntity, DTO extends BaseAPIModel> {
	
	DTO toDTO(Entity from);

	List<DTO> toDTOList(List<Entity> from);

	Entity toEntity(DTO from);
	
}
