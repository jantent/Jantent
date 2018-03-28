package springboot.modal.bo;

import java.io.Serializable;

/**
 * @author tangj
 * @date 2018/1/23 11:55
 */
public class StaticticsBo implements Serializable{
    private Long articles;
    private Long comments;
    private Long links;
    private Long attachs;

    public Long getArticles() {
        return articles;
    }

    public void setArticles(Long articles) {
        this.articles = articles;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLinks() {
        return links;
    }

    public void setLinks(Long links) {
        this.links = links;
    }

    public Long getAttachs() {
        return attachs;
    }

    public void setAttachs(Long attachs) {
        this.attachs = attachs;
    }

    @Override
    public String toString() {
        return "StaticticsBo{" +
                "articles=" + articles +
                ", comments=" + comments +
                ", links=" + links +
                ", attachs=" + attachs +
                '}';
    }
}
