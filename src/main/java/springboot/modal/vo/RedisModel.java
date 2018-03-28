package springboot.modal.vo;

import java.io.Serializable;

/**
 * redis 模型
 */
public class RedisModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Integer age;

    public RedisModel() {
        super();
    }

    public RedisModel(String id, String name, Integer age) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "RedisModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
