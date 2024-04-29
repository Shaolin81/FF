package cz.ff.jsframework;

import cz.ff.jsframework.api.model.JsFrameworkViewDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JsFrameworkMapper {

    JsFrameworkViewDTO map(JsFrameworkView jsFramework);

    List<JsFrameworkViewDTO> map(List<JsFrameworkView> jsFramework);
}
