package com.example.demo.mapper;

import com.example.demo.dto.postmedia.CreatePostMediaDTO;
import com.example.demo.dto.postmedia.PostMediaDTO;
import com.example.demo.model.interact.PostMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMediaMapper {
    PostMediaMapper INSTANCE = Mappers.getMapper(PostMediaMapper.class);

    PostMediaDTO toDto(PostMedia postMedia);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post.id", source = "postId")
    PostMedia toEntity(CreatePostMediaDTO createPostMediaDTO);
}
