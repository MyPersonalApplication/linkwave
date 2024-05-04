package com.example.demo.mapper;

import com.example.demo.dto.post.CreatePostDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.interact.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "lstMedia", source = "postMedia")
    PostDTO toDto(Post post);

    @Mapping(target = "user.id", source = "userId")
    Post toEntity(CreatePostDTO createPostDTO);
}
