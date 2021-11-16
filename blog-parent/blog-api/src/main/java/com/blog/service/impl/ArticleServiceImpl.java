package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dao.dos.Archives;
import com.blog.dao.mapper.ArticleBodyMapper;
import com.blog.dao.mapper.ArticleMapper;
import com.blog.dao.mapper.ArticleTagMapper;
import com.blog.dao.pojo.Article;
import com.blog.dao.pojo.ArticleBody;
import com.blog.service.*;
import com.blog.vo.ArticleBodyVo;
import com.blog.vo.ArticleVo;
import com.blog.vo.Result;
import com.blog.vo.params.PageParams;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private ThreadService threadService;

    /**
     * 首页，展示文章列表
     * @param pageParams
     * @return
     */
    @Override
    public Result listArticlesPage(PageParams pageParams) {
        Page<Article> page = new Page<Article>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true,false,false));
    }


    /**
     * List<Article>转换为List<ArticleVo>，便于展示
     * @param records
     * @param isTag
     * @param isAuthor
     * @param isBody
     * @param isCategory
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<ArticleVo>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签，作者信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserVoById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            if (categoryId != null){
                articleVo.setCategory(categoryService.findCategoryById(categoryId));
            }
        }
        return articleVo;
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


    /**
     * 最热文章
     * @param limit
     * @return
     */
    @Override
    public Result hotArticle(int limit) {
        List<Article> articles = articleMapper.hotArticle(limit);
        return Result.success(articles);
    }

    /**
     * 首页 最新文章
     * @param limit
     * @return
     */
    public Result newArticles(int limit) {
        List<Article> articles = articleMapper.newArticles(limit);
        return Result.success(articles);
    }

    /**
     * 归档文章
     * @return
     */
    public Result listArchives(){
        List<Archives> archives = articleMapper.listArchives();
        return Result.success(archives);
    }

    /**
     * 根据id查看文章详情
     * @param articleId
     * @return
     */
    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        //long start = System.currentTimeMillis();
        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
        //查看完文章了，新增阅读数，有没有问题呢？
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        // 更新 增加了此次接口的 耗时 如果一旦更新出问题，不能影响 查看文章的操作
        //线程池  可以把更新操作 扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        //long end = System.currentTimeMillis();
        return Result.success(articleVo);
    }
}
