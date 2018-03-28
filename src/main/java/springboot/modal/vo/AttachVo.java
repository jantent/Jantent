package springboot.modal.vo;

import java.io.Serializable;

/**
 * @author tangj
 * @date 2018/1/21 11:08
 */
public class AttachVo implements Serializable {
    private Integer id;

    private String fname;

    private String ftype;

    private String fkey;

    private Integer authorId;

    private Integer created;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "AttachVo{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", ftype='" + ftype + '\'' +
                ", fkey='" + fkey + '\'' +
                ", authorId=" + authorId +
                ", created=" + created +
                '}';
    }
}
