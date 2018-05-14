package springboot.controller.admin;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springboot.constant.WebConst;
import springboot.controller.AbstractController;
import springboot.controller.helper.ExceptionHelper;
import springboot.dto.LogActions;
import springboot.dto.Types;
import springboot.exception.TipException;
import springboot.modal.bo.RestResponseBo;
import springboot.modal.vo.AttachVo;
import springboot.modal.vo.UserVo;
import springboot.service.IAttachService;
import springboot.service.ILogService;
import springboot.util.Commons;
import springboot.util.MyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 附件管理
 *
 * @author tangj
 * @date 2018/1/31 23:14
 */
@Controller
@RequestMapping("admin/attach")
public class AttachController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(AttachController.class);

    public static final String CLASSPATH = MyUtils.getUploadFilePath();

    @Resource
    private IAttachService attachService;

    @Resource
    private ILogService logService;

    /**
     * 附件页面
     *
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "12") int limit) {
        PageInfo<AttachVo> attachPagination = attachService.getAttachs(page, limit);
        request.setAttribute("attachs", attachPagination);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType()));
        request.setAttribute("max_file_size", WebConst.MAX_TEXT_COUNT / 1024);
        return "admin/attach";
    }

    /**
     * 上次附件post
     *
     * @param request
     * @param multipartFiles
     * @return
     * @throws IOException
     */
    @PostMapping(value = "upload")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo upload(HttpServletRequest request, @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {
        UserVo users = this.user(request);
        Integer uid = users.getUid();
        // 记录上传成功的文件信息
        List<AttachVo> attachVoList = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String name = multipartFile.getOriginalFilename();
                if (multipartFile.getSize() <= WebConst.MAX_FILE_SIZE) {
                    // 获取文件相对路径名，并文件目录
                    String fkey = MyUtils.getFileKey(name);
                    // 判断文件是否是图片
                    String ftype = MyUtils.isImage(multipartFile.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType();
                    File file = new File(CLASSPATH + fkey);
                    FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
                    attachService.save(name, fkey, ftype, uid);
                    AttachVo attach = new AttachVo();
                    attach.setFkey(fkey);
                    attachVoList.add(attach);
                }
            }
        } catch (Exception e) {
            return RestResponseBo.fail("文件上传失败");
        }
        return RestResponseBo.ok(attachVoList);
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam Integer id, HttpServletRequest request) {
        try {
            AttachVo attach = attachService.selectById(id);
            if (null == attach) {
                return RestResponseBo.fail("不存在该附件");
            }
            attachService.deleteById(id);
            new File(CLASSPATH + attach.getFkey()).delete();
            logService.insertLog(LogActions.DEL_ATTACH.getAction(), attach.getFkey(), request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "附件删除失败";
            return ExceptionHelper.handlerException(logger, msg, e);
        }
        return RestResponseBo.ok();
    }
}
