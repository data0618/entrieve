package happy.liu.dao;

/**
 * 基本操作
 * @author ljp
 *
 */
public interface BaseActImpl {
	/**
	 * 创建索引
	 */
	void createIndex();
	
	/**
	 * 查询
	 */
	void readIndex();
	
	/**
	 * 根据条件索引
	 */
	void queryIndex();
	/**
	 * 根据条件索引
	 */
	void queryIndex(String index, String field);
	
	/**
	 * 更新索引
	 */
	void updateIndex();
	
	/**
	 * 删除索引
	 */
	void deleteIndex();
	
}
