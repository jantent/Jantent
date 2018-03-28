package springboot.modal.vo;

import java.io.Serializable;

/**
 * @author tangj
 * @date 2018/1/21 11:17
 */
public class RelationshipVo implements Serializable{
    /**
     * 内容主键
     */
    private Integer cid;

    /**
     * 项目主键
     */
    private Integer mid;

    private static final long serialVersionUID = 1L;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
