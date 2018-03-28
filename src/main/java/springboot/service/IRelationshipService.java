package springboot.service;

import springboot.modal.vo.RelationshipVoKey;

import java.util.List;

/**
 * @author tangj
 * @date 2018/1/24 21:28
 */
public interface IRelationshipService {
    /**
     * 按主键删除
     *
     * @param cid
     * @param mid
     */
    void deleteById(Integer cid, Integer mid);

    /**
     * 按主键统计条数
     *
     * @param cid
     * @param mid
     * @return
     */
    Long countById(Integer cid, Integer mid);

    /**
     * 保存对象
     *
     * @param relationshipVoKey
     */
    void insertVo(RelationshipVoKey relationshipVoKey);

    /**
     * 根据id搜索
     *
     * @param cid
     * @param mid
     * @return
     */
    List<RelationshipVoKey> getRelationshipById(Integer cid, Integer mid);
}
