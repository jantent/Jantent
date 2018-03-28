package springboot.service;

import com.github.pagehelper.PageInfo;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.ContentVoExample;

/**
 * @author tangj
 * @date 2018/1/24 21:04
 */
public interface IContentService {
    /**
     * 发布文章
     *
     * @param contents
     */
    void publish(ContentVo contents);

    /**
     * 查询文章返回对跳数据
     *
     * @param p
     * @param limit
     * @return
     */
    PageInfo<ContentVo> getContents(Integer p, Integer limit);


    /**
     * 根据id或slug获取文章
     *
     * @param id id
     * @return ContentVo
     */
    ContentVo getContents(String id);

    /**
     * 根据主键更新
     *
     * @param contentVo contentVo
     */
    void updateContentByCid(ContentVo contentVo);

    /**
     * 查询分类/标签下的文章归档
     *
     * @param mid   mid
     * @param page  page
     * @param limit limit
     * @return ContentVo
     */
    PageInfo<ContentVo> getArticles(Integer mid, int page, int limit);

    /**
     * 搜索、分页
     *
     * @param keyword keyword
     * @param page    page
     * @param limit   limit
     * @return ContentVo
     */
    PageInfo<ContentVo> getArticles(String keyword, Integer page, Integer limit);

    /**
     * @param commentVoExample
     * @param page
     * @param limit
     * @return
     */
    PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit);

    /**
     * 根据文章id删除
     *
     * @param cid
     */
    void deleteByCid(Integer cid);

    /**
     * 编辑文章
     *
     * @param contents
     */
    void updateArticle(ContentVo contents);


    /**
     * 更新原有文章的category
     *
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal, String newCatefory);
}
