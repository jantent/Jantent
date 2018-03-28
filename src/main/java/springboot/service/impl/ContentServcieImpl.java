package springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springboot.constant.WebConst;
import springboot.dao.ContentVoMapper;
import springboot.dao.MetaVoMapper;
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.ContentVoExample;
import springboot.service.IContentService;
import springboot.service.IMetaService;
import springboot.service.IRelationshipService;
import springboot.util.DateKit;
import springboot.util.MyUtils;
import springboot.util.Tools;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangj
 * @date 2018/1/24 21:21
 */
@Service
public class ContentServcieImpl implements IContentService {

    @Resource
    private ContentVoMapper contentDao;

    @Resource
    private MetaVoMapper metaDao;

    @Resource
    private IRelationshipService relationshipService;

    @Resource
    private IMetaService metasService;

    @Override
    public void publish(ContentVo contents) {
        if (null == contents) {
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        int titleLength = contents.getTitle().length();
        if (titleLength > WebConst.MAX_TITLE_COUNT) {
            throw new TipException("文章标题过长");
        }
        int contentLength = contents.getContent().length();
        if (contentLength > WebConst.MAX_TEXT_COUNT) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isNotBlank(contents.getSlug())) {
            if (contents.getSlug().length() < 5) {
                throw new TipException("路径太短了");
            }
            if (!MyUtils.isPath(contents.getSlug())) {
                throw new TipException("您输入的路径不合法");
            }
            ContentVoExample contentVoExample = new ContentVoExample();
            contentVoExample.createCriteria().andTypeEqualTo(contents.getType()).andSlugEqualTo(contents.getSlug());
            long count = contentDao.countByExample(contentVoExample);
            if (count > 0) {
                throw new TipException("该路径已经存在，请重新输入");
            }
        } else {
            contents.setSlug(null);
        }
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateKit.getCurrentUnixTime();
        contents.setCreated(time);
        contents.setModified(time);
        contents.setHits(0);
        contents.setCommentsNum(0);

        String tags = contents.getTags();
        String categories = contents.getCategories();
        contentDao.insert(contents);
        Integer cid = contents.getCid();

        metasService.saveMetas(cid, tags, Types.TAG.getType());
        metasService.saveMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    public PageInfo<ContentVo> getContents(Integer p, Integer limit) {
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        PageHelper.startPage(p, limit);
        List<ContentVo> contentVoList = contentDao.selectByExampleWithBLOBs(example);
        PageInfo<ContentVo> pageInfo = new PageInfo<>(contentVoList);
        return pageInfo;
    }

    @Override
    public ContentVo getContents(String id) {
        if (StringUtils.isNotBlank(id)) {
            if (Tools.isNumber(id)) {
                ContentVo contentVo = contentDao.selectByPrimaryKey(Integer.valueOf(id));
                if (contentVo != null) {
                    contentVo.setHits(contentVo.getHits() + 1);
                    contentDao.updateByPrimaryKey(contentVo);
                }
                return contentVo;
            } else {
                ContentVoExample contentVoExample = new ContentVoExample();
                contentVoExample.createCriteria().andSlugEqualTo(id);
                List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(contentVoExample);
                if (contentVos.size() != 1) {
                    throw new TipException("query content by id and return is not one");
                }
                return contentVos.get(0);
            }
        }
        return null;
    }

    @Override
    public void updateContentByCid(ContentVo contentVo) {
        if (null != contentVo && null != contentVo.getCid()) {
            contentDao.updateByPrimaryKeySelective(contentVo);
        }
    }

    @Override
    public PageInfo<ContentVo> getArticles(Integer mid, int page, int limit) {
        int total = metaDao.countWithSql(mid);
        PageHelper.startPage(page, limit);
        List<ContentVo> list = contentDao.findByCatalog(mid);
        PageInfo<ContentVo> paginator = new PageInfo<>(list);
        paginator.setTotal(total);
        return paginator;
    }

    @Override
    public PageInfo<ContentVo> getArticles(String keyword, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        ContentVoExample contentVoExample = new ContentVoExample();
        ContentVoExample.Criteria criteria = contentVoExample.createCriteria();
        criteria.andTypeEqualTo(Types.ARTICLE.getType());
        criteria.andStatusEqualTo(Types.PUBLISH.getType());
        criteria.andTitleLike("%" + keyword + "%");
        contentVoExample.setOrderByClause("created desc");
        List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(contentVoExample);
        return new PageInfo<>(contentVos);
    }

    @Override
    public PageInfo<ContentVo> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ContentVo> contentVos = contentDao.selectByExampleWithBLOBs(commentVoExample);
        return new PageInfo<>(contentVos);
    }

    @Override
    public void deleteByCid(Integer cid) {
        ContentVo contentVo = this.getContents(cid + "");
        if (null != contentVo) {
            contentDao.deleteByPrimaryKey(cid);
            relationshipService.deleteById(cid, null);
        }
    }

    @Override
    public void updateArticle(ContentVo contents) {
        if (null == contents || null == contents.getCid()) {
            throw new TipException("文章对象不能为空");
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (contents.getTitle().length() > 200) {
            throw new TipException("文章标题过长");
        }
        if (contents.getContent().length() > 65000) {
            throw new TipException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TipException("请登录后发布文章");
        }
        if (StringUtils.isBlank(contents.getSlug())) {
            contents.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contents.setModified(time);
        Integer cid = contents.getCid();
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        contentDao.updateByPrimaryKeySelective(contents);
        relationshipService.deleteById(cid, null);
        metasService.saveMetas(cid, contents.getTags(), Types.TAG.getType());
        metasService.saveMetas(cid, contents.getCategories(), Types.CATEGORY.getType());
    }

    @Override
    public void updateCategory(String ordinal, String newCatefory) {
        ContentVo contentVo = new ContentVo();
        contentVo.setCategories(newCatefory);
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andCategoriesEqualTo(ordinal);
        contentDao.updateByExampleSelective(contentVo, example);
    }
}
