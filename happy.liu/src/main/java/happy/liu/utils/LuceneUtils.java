package happy.liu.utils;

import java.io.IOException;  
import java.nio.file.Paths;  
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.List;  
import java.util.Set;  
import java.util.concurrent.ExecutorService;  
  

import org.apache.lucene.analysis.Analyzer;  
import org.apache.lucene.analysis.hy.ArmenianAnalyzer;
import org.apache.lucene.document.Document;  
import org.apache.lucene.document.Field;  
import org.apache.lucene.document.TextField;  
import org.apache.lucene.index.IndexReader;  
import org.apache.lucene.index.IndexWriter;  
import org.apache.lucene.index.IndexWriterConfig;  
import org.apache.lucene.index.IndexableField;  
import org.apache.lucene.index.Term;  
import org.apache.lucene.queryparser.classic.QueryParser;  
import org.apache.lucene.search.IndexSearcher;  
import org.apache.lucene.search.Query;  
import org.apache.lucene.search.ScoreDoc;  
import org.apache.lucene.search.TopDocs;  
import org.apache.lucene.search.highlight.Formatter;  
import org.apache.lucene.search.highlight.Fragmenter;  
import org.apache.lucene.search.highlight.Highlighter;  
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;  
import org.apache.lucene.search.highlight.QueryScorer;  
import org.apache.lucene.search.highlight.Scorer;  
import org.apache.lucene.search.highlight.SimpleFragmenter;  
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;  
import org.apache.lucene.store.Directory;  
import org.apache.lucene.store.FSDirectory;  
  
/** 
 * Lucene������(����Lucene5.0��װ) 
 * @author Lanxiaowei 
 * 
 */  
public class LuceneUtils {  
    private static final LuceneManager luceneManager = LuceneManager.getInstance();  
    private static Analyzer analyzer = new ArmenianAnalyzer();  
      
    /** 
     * ������Ŀ¼ 
     *  
     * @param luceneDir 
     * @return 
     * @throws IOException 
     */  
    public static FSDirectory openFSDirectory(String luceneDir) {  
        FSDirectory directory = null;  
        try {  
            directory = FSDirectory.open(Paths.get(luceneDir));  
            /** 
             * ע�⣺isLocked�����ڲ�����ͼȥ��ȡLock,�����ȡ��Lock����ر���������return false��ʾ����Ŀ¼û�б����� 
             * ��Ҳ����Ϊʲôunlock��������IndexWriter�����Ƴ���ԭ�� 
             */  
            IndexWriter.isLocked(directory);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return directory;  
    }  
      
    /** 
     * �ر�����Ŀ¼������ 
     * @param directory 
     * @throws IOException 
     */  
    public static void closeDirectory(Directory directory) throws IOException {  
        if (null != directory) {  
            directory.close();  
            directory = null;  
        }  
    }  
      
    /** 
     * ��ȡIndexWriter 
     * @param dir 
     * @param config 
     * @return 
     */  
    public static IndexWriter getIndexWrtier(Directory dir, IndexWriterConfig config) {  
        return luceneManager.getIndexWriter(dir, config);  
    }  
      
    /** 
     * ��ȡIndexWriter 
     * @param dir 
     * @param config 
     * @return 
     */  
    public static IndexWriter getIndexWrtier(String directoryPath, IndexWriterConfig config) {  
        FSDirectory directory = openFSDirectory(directoryPath);  
        return luceneManager.getIndexWriter(directory, config);  
    }  
      
    /** 
     * ��ȡIndexReader 
     * @param dir 
     * @param enableNRTReader  �Ƿ���NRTReader 
     * @return 
     */  
    public static IndexReader getIndexReader(Directory dir,boolean enableNRTReader) {  
        return luceneManager.getIndexReader(dir, enableNRTReader);  
    }  
      
    /** 
     * ��ȡIndexReader(Ĭ�ϲ�����NRTReader) 
     * @param dir 
     * @return 
     */  
    public static IndexReader getIndexReader(Directory dir) {  
        return luceneManager.getIndexReader(dir);  
    }  
      
    /** 
     * ��ȡIndexSearcher 
     * @param reader    IndexReader���� 
     * @param executor  �������Ҫ�������̲߳�ѯ�����ṩExecutorService������� 
     * @return 
     */  
    public static IndexSearcher getIndexSearcher(IndexReader reader,ExecutorService executor) {  
        return luceneManager.getIndexSearcher(reader, executor);  
    }  
      
    /** 
     * ��ȡIndexSearcher(��֧�ֶ��̲߳�ѯ) 
     * @param reader    IndexReader���� 
     * @return 
     */  
    public static IndexSearcher getIndexSearcher(IndexReader reader) {  
        return luceneManager.getIndexSearcher(reader);  
    }  
      
    /** 
     * ����QueryParser���� 
     * @param field 
     * @param analyzer 
     * @return 
     */  
    public static QueryParser createQueryParser(String field, Analyzer analyzer) {  
        return new QueryParser(field, analyzer);  
    }  
      
    /** 
     * �ر�IndexReader 
     * @param reader 
     */  
    public static void closeIndexReader(IndexReader reader) {  
        if (null != reader) {  
            try {  
                reader.close();  
                reader = null;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    /** 
     * �ر�IndexWriter 
     * @param writer 
     */  
    public static void closeIndexWriter(IndexWriter writer) {  
        if(null != writer) {  
            try {  
                writer.close();  
                writer = null;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
      
    /** 
     * �ر�IndexReader��IndexWriter 
     * @param reader 
     * @param writer 
     */  
    public static void closeAll(IndexReader reader, IndexWriter writer) {  
        closeIndexReader(reader);  
        closeIndexWriter(writer);  
    }  
      
    /** 
     * ɾ������[ע�⣺���Լ��ر�IndexWriter����] 
     * @param writer 
     * @param field 
     * @param value 
     */  
    public static void deleteIndex(IndexWriter writer, String field, String value) {  
        try {  
            writer.deleteDocuments(new Term[] {new Term(field,value)});  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ɾ������[ע�⣺���Լ��ر�IndexWriter����] 
     * @param writer 
     * @param query 
     */  
    public static void deleteIndex(IndexWriter writer, Query query) {  
        try {  
            writer.deleteDocuments(query);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ����ɾ������[ע�⣺���Լ��ر�IndexWriter����] 
     * @param writer 
     * @param terms 
     */  
    public static void deleteIndexs(IndexWriter writer,Term[] terms) {  
        try {  
            writer.deleteDocuments(terms);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ����ɾ������[ע�⣺���Լ��ر�IndexWriter����] 
     * @param writer 
     * @param querys 
     */  
    public static void deleteIndexs(IndexWriter writer,Query[] querys) {  
        try {  
            writer.deleteDocuments(querys);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ɾ�����������ĵ� 
     * @param writer 
     */  
    public static void deleteAllIndex(IndexWriter writer) {  
        try {  
            writer.deleteAll();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ���������ĵ� 
     * @param writer 
     * @param term 
     * @param document 
     */  
    public static void updateIndex(IndexWriter writer,Term term,Document document) {  
        try {  
            writer.updateDocument(term, document);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * ���������ĵ� 
     * @param writer 
     * @param term 
     * @param document 
     */  
    public static void updateIndex(IndexWriter writer,String field,String value,Document document) {  
        updateIndex(writer, new Term(field, value), document);  
    }  
      
    /** 
     * ��������ĵ� 
     * @param writer 
     * @param doc 
     */  
    public static void addIndex(IndexWriter writer, Document document) {  
        updateIndex(writer, null, document);  
    }  
      
    /** 
     * �����ĵ���ѯ 
     * @param searcher 
     * @param query 
     * @return 
     */  
    public static List<Document> query(IndexSearcher searcher,Query query) {  
        TopDocs topDocs = null;  
        try {  
            topDocs = searcher.search(query, Integer.MAX_VALUE);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        ScoreDoc[] scores = topDocs.scoreDocs;  
        int length = scores.length;  
        if (length <= 0) {  
            return Collections.emptyList();  
        }  
        List<Document> docList = new ArrayList<Document>();  
        try {  
            for (int i = 0; i < length; i++) {  
                Document doc = searcher.doc(scores[i].doc);  
                docList.add(doc);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return docList;  
    }  
      
    /** 
     * ���������ĵ�������[ע�⣺���Լ��ֶ��ر�IndexReader] 
     * @param reader 
     * @return 
     */  
    public static int getIndexTotalCount(IndexReader reader) {  
        return reader.numDocs();  
    }  
      
    /** 
     * ���������ĵ�������ĵ�ID[ע�⣺���Լ��ֶ��ر�IndexReader] 
     * @param reader 
     * @return 
     */  
    public static int getMaxDocId(IndexReader reader) {  
        return reader.maxDoc();  
    }  
      
    /** 
     * �����Ѿ�ɾ����δ�ύ���ĵ�����[ע�⣺���Լ��ֶ��ر�IndexReader] 
     * @param reader 
     * @return 
     */  
    public static int getDeletedDocNum(IndexReader reader) {  
        return getMaxDocId(reader) - getIndexTotalCount(reader);  
    }  
      
    /** 
     * ����docId��ѯ�����ĵ� 
     * @param reader         IndexReader���� 
     * @param docID          documentId 
     * @param fieldsToLoad   ��Ҫ���ص�field 
     * @return 
     */  
    public static Document findDocumentByDocId(IndexReader reader,int docID, Set<String> fieldsToLoad) {  
        try {  
            return reader.document(docID, fieldsToLoad);  
        } catch (IOException e) {  
            return null;  
        }  
    }  
      
    /** 
     * ����docId��ѯ�����ĵ� 
     * @param reader         IndexReader���� 
     * @param docID          documentId 
     * @return 
     */  
    public static Document findDocumentByDocId(IndexReader reader,int docID) {  
        return findDocumentByDocId(reader, docID, null);  
    }  
      
    /** 
     * @Title: createHighlighter 
     * @Description: ���������� 
     * @param query             ������ѯ���� 
     * @param prefix            ����ǰ׺�ַ��� 
     * @param stuffix           ������׺�ַ��� 
     * @param fragmenterLength  ժҪ��󳤶� 
     * @return 
     */  
    public static Highlighter createHighlighter(Query query, String prefix, String stuffix, int fragmenterLength) {  
        Formatter formatter = new SimpleHTMLFormatter((prefix == null || prefix.trim().length() == 0) ?   
            "<font color=\"red\">" : prefix, (stuffix == null || stuffix.trim().length() == 0)?"</font>" : stuffix);  
        Scorer fragmentScorer = new QueryScorer(query);  
        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);  
        Fragmenter fragmenter = new SimpleFragmenter(fragmenterLength <= 0 ? 50 : fragmenterLength);  
        highlighter.setTextFragmenter(fragmenter);  
        return highlighter;  
    }  
      
    /** 
     * @Title: highlight 
     * @Description: ���ɸ����ı� 
     * @param document          �����ĵ����� 
     * @param highlighter       ������ 
     * @param analyzer          �����ִ��� 
     * @param field             �����ֶ� 
     * @return 
     * @throws IOException 
     * @throws InvalidTokenOffsetsException 
     */  
    public static String highlight(Document document,Highlighter highlighter,Analyzer analyzer,String field) throws IOException {  
        List<IndexableField> list = document.getFields();  
        for (IndexableField fieldable : list) {  
            String fieldValue = fieldable.stringValue();  
            if(fieldable.name().equals(field)) {  
                try {  
                    fieldValue = highlighter.getBestFragment(analyzer, field, fieldValue);  
                } catch (InvalidTokenOffsetsException e) {  
                    fieldValue = fieldable.stringValue();  
                }  
                return (fieldValue == null || fieldValue.trim().length() == 0)? fieldable.stringValue() : fieldValue;  
            }  
        }  
        return null;  
    }  
      
    /** 
     * @Title: searchTotalRecord 
     * @Description: ��ȡ�����������ܼ�¼�� 
     * @param query 
     * @return 
     * @throws IOException 
     */  
    public static int searchTotalRecord(IndexSearcher search,Query query) {  
        ScoreDoc[] docs = null;  
        try {  
            TopDocs topDocs = search.search(query, Integer.MAX_VALUE);  
            if(topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length == 0) {  
                return 0;  
            }  
            docs = topDocs.scoreDocs;  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return docs.length;  
    }  
      
    /** 
     * @Title: pageQuery 
     * @Description: Lucene��ҳ��ѯ 
     * @param searcher 
     * @param query 
     * @param page 
     * @throws IOException 
     */  
    public static void pageQuery(IndexSearcher searcher,Directory directory,Query query,Page<Document> page) {  
        int totalRecord = searchTotalRecord(searcher,query);  
        //�����ܼ�¼��  
        page.setTotalRecord(totalRecord);  
        TopDocs topDocs = null;  
        try {  
            topDocs = searcher.searchAfter(page.getAfterDoc(),query, page.getPageSize());  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        List<Document> docList = new ArrayList<Document>();  
        ScoreDoc[] docs = topDocs.scoreDocs;  
        int index = 0;  
        for (ScoreDoc scoreDoc : docs) {  
            int docID = scoreDoc.doc;  
            Document document = null;  
            try {  
                document = searcher.doc(docID);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            if(index == docs.length - 1) {  
                page.setAfterDoc(scoreDoc);  
                page.setAfterDocId(docID);  
            }  
            docList.add(document);  
            index++;  
        }  
        page.setItems(docList);  
        closeIndexReader(searcher.getIndexReader());  
    }  
      
    /** 
     * @Title: pageQuery 
     * @Description: ��ҳ��ѯ[��������˸���,�����������ĵ�] 
     * @param searcher 
     * @param directory 
     * @param query 
     * @param page 
     * @param highlighterParam 
     * @param writerConfig 
     * @throws IOException 
     */  
    public static void pageQuery(IndexSearcher searcher,Directory directory,Query query,Page<Document> page,HighlighterParam highlighterParam,IndexWriterConfig writerConfig) throws IOException {  
        IndexWriter writer = null;  
        //��δ���ø���  
        if(null == highlighterParam || !highlighterParam.isHighlight()) {  
            pageQuery(searcher,directory,query, page);  
        } else {  
            int totalRecord = searchTotalRecord(searcher,query);  
            System.out.println("totalRecord:" + totalRecord);  
            //�����ܼ�¼��  
            page.setTotalRecord(totalRecord);  
            TopDocs topDocs = searcher.searchAfter(page.getAfterDoc(),query, page.getPageSize());  
            List<Document> docList = new ArrayList<Document>();  
            ScoreDoc[] docs = topDocs.scoreDocs;  
            int index = 0;  
            writer = getIndexWrtier(directory, writerConfig);  
            for (ScoreDoc scoreDoc : docs) {  
                int docID = scoreDoc.doc;  
                Document document = searcher.doc(docID);  
                String content = document.get(highlighterParam.getFieldName());  
                if(null != content && content.trim().length() > 0) {  
                    //����������  
                    Highlighter highlighter = LuceneUtils.createHighlighter(query,   
                        highlighterParam.getPrefix(), highlighterParam.getStuffix(),   
                        highlighterParam.getFragmenterLength());  
                    String text = highlight(document, highlighter, analyzer, highlighterParam.getFieldName());  
                    //���������ԭʼ�ı�����ͬ����ʾ�����ɹ�  
                    if(!text.equals(content)) {  
                        Document tempdocument = new Document();  
                        List<IndexableField> indexableFieldList = document.getFields();  
                        if(null != indexableFieldList && indexableFieldList.size() > 0) {  
                            for(IndexableField field : indexableFieldList) {  
                                if(field.name().equals(highlighterParam.getFieldName())) {  
                                    tempdocument.add(new TextField(field.name(), text, Field.Store.YES));  
                                } else {  
                                    tempdocument.add(field);  
                                }  
                            }  
                        }  
                        updateIndex(writer, new Term(highlighterParam.getFieldName(),content), tempdocument);  
                        document = tempdocument;  
                    }  
                }  
                if(index == docs.length - 1) {  
                    page.setAfterDoc(scoreDoc);  
                    page.setAfterDocId(docID);  
                }  
                docList.add(document);  
                index++;  
            }  
            page.setItems(docList);  
        }  
        closeIndexReader(searcher.getIndexReader());  
        closeIndexWriter(writer);  
    }  
}  