package happy.liu.utils;

import java.io.IOException;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.locks.Lock;  
import java.util.concurrent.locks.ReentrantLock;  
  
import org.apache.lucene.index.DirectoryReader;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.IndexWriter;  
import org.apache.lucene.index.IndexWriterConfig;  
import org.apache.lucene.search.IndexSearcher;  
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.LockObtainFailedException;  
/** 
 * Lucene������д��/��ѯ��������ȡ������  
 * 
 */  
public class LuceneManager {  
	
    private volatile static LuceneManager singleton;  
      
    private volatile static IndexWriter writer;  
      
    private volatile static IndexReader reader;  
      
    private volatile static IndexSearcher searcher;  
      
    private final Lock writerLock = new ReentrantLock();  
      
    //private final Lock readerLock = new ReentrantLock();  
      
    //private final Lock searcherLock = new ReentrantLock();  
  
    private LuceneManager() {}  
  
    public static LuceneManager getInstance() {  
        if (null == singleton) {  
            synchronized (LuceneManager.class) {  
                if (null == singleton) {  
                    singleton = new LuceneManager();  
                }  
            }  
        }  
        return singleton;  
    }  
  
    /** 
     * ��ȡIndexWriter�������� 
     * @param dir 
     * @param config 
     * @return 
     */  
    public IndexWriter getIndexWriter(Directory dir, IndexWriterConfig config) {  
        if(null == dir) {  
            throw new IllegalArgumentException("Directory can not be null.");  
        }  
        if(null == config) {  
            throw new IllegalArgumentException("IndexWriterConfig can not be null.");  
        }  
        try {  
            writerLock.lock();  
            if(null == writer){  
                //�������Ŀ¼��������ֱ�����쳣  
                if(IndexWriter.isLocked(dir)) {  
                    throw new LockObtainFailedException("Directory of index had been locked.");  
                }  
                writer = new IndexWriter(dir, config);  
            }  
        } catch (LockObtainFailedException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            writerLock.unlock();  
        }  
        return writer;  
    }  
      
    /** 
     * ��ȡIndexReader���� 
     * @param dir 
     * @param enableNRTReader  �Ƿ���NRTReader 
     * @return 
     */  
    public IndexReader getIndexReader(Directory dir,boolean enableNRTReader) {  
        if(null == dir) {  
            throw new IllegalArgumentException("Directory can not be null.");  
        }  
        try {  
            if(null == reader){  
                reader = DirectoryReader.open(dir);  
            } else {  
                if(enableNRTReader && reader instanceof DirectoryReader) {  
                    //������ʵʱReader,������������̬���/ɾ���������仯  
                    reader = DirectoryReader.openIfChanged((DirectoryReader)reader);  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return reader;  
    }  
      
    /** 
     * ��ȡIndexReader����(Ĭ�ϲ�����NETReader) 
     * @param dir 
     * @return 
     */  
    public IndexReader getIndexReader(Directory dir) {  
        return getIndexReader(dir, false);  
    }  
      
    /** 
     * ��ȡIndexSearcher���� 
     * @param reader    IndexReader����ʵ�� 
     * @param executor  �������Ҫ�������̲߳�ѯ�����ṩExecutorService������� 
     * @return 
     */  
    public IndexSearcher getIndexSearcher(IndexReader reader,ExecutorService executor) {  
        if(null == reader) {  
            throw new IllegalArgumentException("The indexReader can not be null.");  
        }  
        if(null == searcher){  
            searcher = new IndexSearcher(reader);  
        }  
        return searcher;  
    }  
      
    /** 
     * ��ȡIndexSearcher����(��֧�ֶ��̲߳�ѯ) 
     * @param reader    IndexReader����ʵ�� 
     * @return 
     */  
    public IndexSearcher getIndexSearcher(IndexReader reader) {  
        return getIndexSearcher(reader, null);  
    }  
}  