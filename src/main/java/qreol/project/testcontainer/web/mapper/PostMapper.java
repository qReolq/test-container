package qreol.project.testcontainer.web.mapper;

import org.mapstruct.Mapper;
import qreol.project.testcontainer.model.Post;
import qreol.project.testcontainer.web.dto.PostDto;

@Mapper(componentModel = "spring")
public interface PostMapper extends Mappable<Post, PostDto> {
}
