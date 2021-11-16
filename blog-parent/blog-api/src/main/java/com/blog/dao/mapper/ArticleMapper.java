package com.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dao.dos.Archives;
import com.blog.dao.pojo.Article;
import com.blog.vo.Result;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();

    /**
     * 首页，展示文章列表
     * @param
     * @return
     */
    IPage<Article> listArticle(Page<Article> page,
                               Long categoryId,
                               Long tagId,
                               String year,
                               String month);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    List<Article> hotArticle(int limit);

    /**
     * 首页 最新文章
     * @param limit
     * @return
     */
    public List<Article> newArticles(int limit);

    /**
     * 更新阅读数
     */
    public void updateViewCounts(Long id,int viewCounts);
}
