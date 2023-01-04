package com.example.springblogproject.service;

import com.example.springblogproject.dto.PostRequestDto;
import com.example.springblogproject.dto.PostResponseDto;
import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.PostLike;
import com.example.springblogproject.repository.CommentRepository;
import com.example.springblogproject.repository.PostLikeRepository;
import com.example.springblogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public List<PostResponseDto> getAllPost(){
        List<PostResponseDto> list = new ArrayList<>();
        for(Post post :postRepository.findAllByOrderByCreatedAtDesc()){
            List<Comment> commentList = commentRepository.findByPostIdOrderByCreatedAtDesc(post.getId());
            list.add(new PostResponseDto(post, commentList));
        }
        return list;
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) {
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Post post = new Post(title, content, username);

        postRepository.save(post);
        List<Comment> commentList = new ArrayList<>();
        return new PostResponseDto(post, commentList);
    }

    @Transactional
    public PostResponseDto getPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        List<Comment> commentList = commentRepository.findByPostIdOrderByCreatedAtDesc(post.getId());
        return new PostResponseDto(post, commentList);
    }

    @Transactional
    public PostResponseDto updateMyPost(Long id, PostRequestDto postRequestDto, String username){
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        if(post.isEqualUsername(username)) {
            post.update(title, content);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("자신의 글만 수정할 수 있습니다.");
        }
        List<Comment> commentList = commentRepository.findByPostIdOrderByCreatedAtDesc(post.getId());
        return new PostResponseDto(post, commentList);
    }

    @Transactional
    public PostResponseDto updateAdminPost(Long id, PostRequestDto postRequestDto){
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        post.update(title, content);
        postRepository.save(post);
        List<Comment> commentList = commentRepository.findByPostIdOrderByCreatedAtDesc(post.getId());
        return new PostResponseDto(post, commentList);
    }

    @Transactional
    public void deleteMyPost(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        if(post.isEqualUsername(username)) {
            commentRepository.deleteByPostId(post.getId());
            postRepository.delete(post);
        } else {
            throw new IllegalArgumentException("자신의 글만 삭제할 수 있습니다.");
        }
    }

    @Transactional
    public void deleteAdminPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        commentRepository.deleteByPostId(post.getId());
        postRepository.delete(post);
    }
    @Transactional
    public String updateLikePost(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        for(PostLike postLike: post.getPostLikes()){
            if(postLike.getUsername().equals(username)){
                postLikeRepository.delete(postLike);
                return "minus";
            }
        }
        postLikeRepository.save(new PostLike(post,username));
        return "plus";
    }
}
