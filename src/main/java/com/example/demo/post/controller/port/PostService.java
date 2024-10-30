package com.example.demo.post.controller.port;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

/**
 * packageName    : com.example.demo.post.controller.port
 * fileName      : PostService
 * owner         : (주)Cobosys
 * date          : 10/30/24 8:01 AM
 * description   :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 10/30/24           doy            최초 생성
 */
public interface PostService {

    Post getById(long id);

    Post create(PostCreate postCreate);

    Post update(long id, PostUpdate postUpdate);
}
