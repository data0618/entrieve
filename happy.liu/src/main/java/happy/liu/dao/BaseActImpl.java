package happy.liu.dao;

/**
 * ��������
 * @author ljp
 *
 */
public interface BaseActImpl {
	/**
	 * ��������
	 */
	void createIndex();
	
	/**
	 * ��ѯ
	 */
	void readIndex();
	
	/**
	 * ������������
	 */
	void queryIndex();
	/**
	 * ������������
	 */
	void queryIndex(String index, String field);
	
	/**
	 * ��������
	 */
	void updateIndex();
	
	/**
	 * ɾ������
	 */
	void deleteIndex();
	
}
