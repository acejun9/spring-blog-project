package com.example.springblogproject.service;

import com.example.springblogproject.dto.CommentResponseDto;
import com.example.springblogproject.dto.PostRequestDto;
import com.example.springblogproject.dto.PostResponseDto;
import com.example.springblogproject.dto.ReplyCommentResponseDto;
import com.example.springblogproject.entity.Comment;
import com.example.springblogproject.entity.Post;
import com.example.springblogproject.entity.PostLike;
import com.example.springblogproject.entity.ReplyComment;
import com.example.springblogproject.repository.CommentRepository;
import com.example.springblogproject.repository.PostLikeRepository;
import com.example.springblogproject.repository.PostRepository;
import com.example.springblogproject.repository.ReplyCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentService commentService;


    @Transactional
    public List<PostResponseDto> getAllPost(Pageable pageable){
        List<PostResponseDto> list = new ArrayList<>();
        for(Post post :postRepository.findAll(pageable)){
            list.add(new PostResponseDto(post, getCommentResponseDtoListByPost(post)));
        }
        return list;
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) {
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();

        Post post = new Post(title, content, username);
        postRepository.save(post);

        List<CommentResponseDto> comments = new ArrayList<>();
        return new PostResponseDto(post, comments);
    }

    @Transactional
    public PostResponseDto getPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        return new PostResponseDto(post, getCommentResponseDtoListByPost(post));
    }

    @Transactional
    public PostResponseDto updateMyPost(Long id, PostRequestDto postRequestDto, String username){
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        if(!post.isEqualUsername(username)) {
            throw new IllegalArgumentException("자신의 글만 수정할 수 있습니다.");
        }
        post.updateContent(title, content);
        postRepository.save(post);
        return new PostResponseDto(post, getCommentResponseDtoListByPost(post));
    }

    @Transactional
    public PostResponseDto updateAdminPost(Long id, PostRequestDto postRequestDto){
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        post.updateContent(title, content);
        postRepository.save(post);
        return new PostResponseDto(post, getCommentResponseDtoListByPost(post));
    }

    @Transactional
    public void deleteMyPost(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        if(!post.isEqualUsername(username)) {
            throw new IllegalArgumentException("자신의 글만 삭제할 수 있습니다.");
        }
        for(Comment comment: commentRepository.findByPostId(post.getId())){
            commentService.deleteMyCommentById(comment.getId(),comment.getUsername());
        }
        deletePostLike(postLikeRepository.findByPost(post));
        postRepository.delete(post);
    }

    @Transactional
    public void deleteAdminPost(Long id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        for(Comment comment: commentRepository.findByPostId(post.getId())){
            commentService.deleteMyCommentById(comment.getId(),comment.getUsername());
        }
        deletePostLike(postLikeRepository.findByPost(post));
        postRepository.delete(post);
    }

    @Transactional
    public String likePost(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        postLikeRepository.save(new PostLike(post,username));
        post.plusLikeCount();
        return "Like +1";
    }

    @Transactional
    public String cancelLikedPost(Long id, String username){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 글이 존재 하지 않습니다.")
        );
        PostLike postLike = postLikeRepository.findByUsernameAndPostId(username, id).orElseThrow(
                () -> new IllegalArgumentException("not my post")
        );
        postLikeRepository.delete(postLike);
        post.minusLikeCount();
        return "Like -1";
    }

    @Transactional
    public void deletePostLike(List<PostLike> postLikes){
        for(PostLike postLike: postLikes){
            postLike.getPost().minusLikeCount();
            postLikeRepository.delete(postLike);
        }
    }

    @Transactional
    public List<CommentResponseDto> getCommentResponseDtoListByPost(Post post){
        List<CommentResponseDto> comments = new ArrayList<>();
        for(Comment comment: commentRepository.findByPostIdOrderByCreatedAtDesc(post.getId())) {
            List<ReplyCommentResponseDto> replyComments = new ArrayList<>();
            for (ReplyComment replyComment: replyCommentRepository.findByParentIdOrderByCreatedAtDesc(comment.getId())){
                replyComments.add(new ReplyCommentResponseDto(replyComment));
            }
            comments.add(new CommentResponseDto(comment,replyComments));
        }
        return comments;
    }
}
