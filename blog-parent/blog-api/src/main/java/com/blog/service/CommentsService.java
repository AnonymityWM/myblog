package com.blog.service;

import com.blog.dao.pojo.Comment;
import com.blog.vo.Result;

public interface CommentsService {
    /**
     * 根据文章id 查询所有的评论列表
     * @param articleId
     * @return
     */
    Result commentsByArticleId(Long articleId);


}