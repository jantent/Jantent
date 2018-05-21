package springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import springboot.constant.WebConst;
import springboot.dao.ContentVoMapper;
import springboot.dao.MetaVoMapper;
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.redisKey.ContentKey;
import springboot.modal.vo.ContentVo;
import springboot.modal.vo.ContentVoExample;
import springboot.service.IContentService;
import springboot.service.IMetaService;
import springboot.service.IRelationshipService;
import springboot.util.DateKit;
import springboot.util.MyUtils;
import springboot.util.RedisKeyUtil;
import springboot.util.Tools;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    private RedisService redisService;

    @Override
    public void publish(ContentVo contents) {
        checkContent(contents);
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
        // 去除表情
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateKit.getCurrentUnixTime();
        contents.setCreated(time);
        contents.setModified(time);
        contents.setHits(0);
        contents.setCommentsNum(0);

        contentDao.insert(contents);

        String tags = contents.getTags();
        String categories = contents.getCategories();
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
        // 先从redis中读取文章信息
        String contentKey = RedisKeyUtil.getKey(ContentKey.TABLE_NAME, ContentKey.MAJOR_KEY, id);
        ContentVo contentVo = (ContentVo) valueOperations.get(contentKey);
        if (contentVo == null){
            if (StringUtils.isNotBlank(id)) {
                if (Tools.isNumber(id)) {
                    contentVo = contentDao.selectByPrimaryKey(Integer.valueOf(id));
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
                    contentVo = contentVos.get(0);
                    valueOperations.set(contentKey,contentVo);
                    redisService.expireKey(contentKey,ContentKey.LIVE_TIME, TimeUnit.HOURS);
                    return contentVo;
                }
            }
        }
        return contentVo;
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
        // 检查文章输入
        checkContent(contents);
        if (StringUtils.isBlank(contents.getSlug())) {
            contents.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contents.setModified(time);
        Integer cid = contents.getCid();
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        contentDao.updateByPrimaryKeySelective(contents);
        // 更新缓存
        String contentKey  = RedisKeyUtil.getKey(ContentKey.TABLE_NAME, ContentKey.MAJOR_KEY, contents.getSlug());
        redisService.deleteKey(contentKey);

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

    private void  checkContent(ContentVo contents) throws TipException{
        if (null == contents) {
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
    }
}
