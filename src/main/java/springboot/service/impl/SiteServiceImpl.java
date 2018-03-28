package springboot.service.impl;

import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import springboot.constant.WebConst;
import springboot.dao.AttachVoMapper;
import springboot.dao.CommentVoMapper;
import springboot.dao.ContentVoMapper;
import springboot.dao.MetaVoMapper;
import springboot.dto.MetaDto;
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.bo.ArchiveBo;
import springboot.modal.bo.BackResponseBo;
import springboot.modal.bo.StaticticsBo;
import springboot.modal.vo.*;
import springboot.service.ISiteService;
import springboot.util.DateKit;
import springboot.util.MyUtils;
import springboot.util.ZipUtils;
import springboot.util.backup.Backup;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author tangj
 * @date 2018/1/23 13:18
 */
@Service
public class SiteServiceImpl implements ISiteService {

    @Resource
    private CommentVoMapper commentDao;

    @Resource
    private ContentVoMapper contentDao;

    @Resource
    private AttachVoMapper attachDao;

    @Resource
    private MetaVoMapper metaDao;

    @Override
    public List<CommentVo> recentComments(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        CommentVoExample example = new CommentVoExample();
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<CommentVo> byPage = commentDao.selectByExampleWithBLOBs(example);
        return byPage;
    }

    @Override
    public List<ContentVo> recentContents(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andStatusEqualTo(Types.PUBLISH.getType()).andTypeEqualTo(Types.ARTICLE.getType());
        example.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        List<ContentVo> list = contentDao.selectByExample(example);
        return list;
    }

    @Override
    public CommentVo getComment(Integer coid) {
        if (null != coid) {
            return commentDao.selectByPrimaryKey(coid);
        }
        return null;
    }

    @Override
    public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
        BackResponseBo backResponseBo = new BackResponseBo();
        if (bk_type.equals("attach")) {
            if (StringUtils.isBlank(bk_path)) {
                throw new TipException("请输入备份文件存储路径");
            }
            if (!(new File(bk_path).isDirectory())) {
                throw new TipException("请输入一个存在的目录");
            }
            String bkAttachDir = MyUtils.getUploadFilePath() + "upload";
            String bkThemesDir = MyUtils.getUploadFilePath() + "templates/themes";
            String fileName = DateKit.dateFormat(new Date(), fmt) + "_" + MyUtils.getRandomNumber(5) + ".zip";

            String attackPath = bk_path + "/" + "attachs_" + fileName;
            String themesPath = bk_path + "/" + "themes_" + fileName;

            ZipUtils.zipFolder(bkAttachDir, attackPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponseBo.setAttachPath(attackPath);
            backResponseBo.setThemePath(themesPath);
        }
        if (bk_type.equals("db")) {
            String bkAttachDir = MyUtils.getUploadFilePath() + "upload/";
            if (!(new File(bkAttachDir)).isDirectory()) {
                File file = new File(bkAttachDir);
                if (!file.exists()) {
                    file.mkdir();
                }
            }
            String sqlFileName = "tale_" + DateKit.dateFormat(new Date(), fmt) + "_" + MyUtils.getRandomNumber(5) + ".sql";
            String zipFile = sqlFileName.replace(".sql", ".zip");

            Backup backup = new Backup(MyUtils.getNewDataSource().getConnection());
            String sqlContent = backup.execute();
            File sqlFile = new File(bkAttachDir + sqlFileName);
            write(sqlContent, sqlFile, Charset.forName("UTF-8"));

            String zip = bkAttachDir + zipFile;
            ZipUtils.zipFile(sqlFile.getPath(), zip);

            Thread.sleep(500);

            if (!sqlFile.exists()) {
                throw new TipException("数据库备份失败");
            }
            sqlFile.delete();
            backResponseBo.setSqlPath(zipFile);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(zip).delete();
                }
            }, 10 * 1000);
        }
        return backResponseBo;
    }

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();

        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());

        Long articles = contentDao.countByExample(contentVoExample);
        Long comments = commentDao.countByExample(new CommentVoExample());
        Long attachs = attachDao.countByExample(new AttachVoExample());

        MetaVoExample metaVoExample = new MetaVoExample();
        metaVoExample.createCriteria().andTypeEqualTo(Types.LINK.getType());
        Long links = metaDao.countByExample(metaVoExample);

        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        staticticsBo.setAttachs(attachs);
        staticticsBo.setLinks(links);
        return staticticsBo;
    }

    @Override
    public List<ArchiveBo> getArchives() {
        List<ArchiveBo> archiveBoList = contentDao.findReturnArchiveBo();
        if (null != archiveBoList) {
            archiveBoList.forEach(archive -> {
                ContentVoExample example = new ContentVoExample();
                ContentVoExample.Criteria criteria = example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
                example.setOrderByClause("created desc");
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                criteria.andCreatedGreaterThan(start);
                criteria.andCreatedLessThan(end);
                List<ContentVo> contentss = contentDao.selectByExample(example);
                archive.setArticles(contentss);
            });
        }
        return archiveBoList;
    }

    @Override
    public List<MetaDto> metas(String type, String orderBy, int limit) {
        List<MetaDto> retList = null;
        if (StringUtils.isNotBlank(type)) {
            if (StringUtils.isBlank(orderBy)) {
                orderBy = "count desc, a.mid desc";
            }
            if (limit < 1 || limit > WebConst.MAX_POSTS) {
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList = metaDao.selectFromSql(paraMap);
        }
        return retList;
    }

    private void write(String data, File file, Charset charset) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(charset));
        } catch (IOException var8) {
            throw new IllegalStateException(var8);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        }

    }
}
