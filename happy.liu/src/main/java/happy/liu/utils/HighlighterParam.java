package happy.liu.utils;

/** 
 * @ClassName: HighlighterParam 
 * @Description: �������������� 
 * @date 2014-3-30 ����12:22:08 
 */  
public class HighlighterParam {  
    /**�Ƿ���Ҫ���ø���*/  
    private boolean highlight;  
    /**��Ҫ���ø�����������*/  
    private String fieldName;  
    /**����ǰ׺*/  
    private String prefix;  
    /**������׺*/  
    private String stuffix;  
    /**��ʾժҪ��󳤶�*/  
    private int fragmenterLength;  
    public boolean isHighlight() {  
        return highlight;  
    }  
    public void setHighlight(boolean highlight) {  
        this.highlight = highlight;  
    }  
    public String getFieldName() {  
        return fieldName;  
    }  
    public void setFieldName(String fieldName) {  
        this.fieldName = fieldName;  
    }  
    public String getPrefix() {  
        return prefix;  
    }  
    public void setPrefix(String prefix) {  
        this.prefix = prefix;  
    }  
    public String getStuffix() {  
        return stuffix;  
    }  
    public void setStuffix(String stuffix) {  
        this.stuffix = stuffix;  
    }  
    public int getFragmenterLength() {  
        return fragmenterLength;  
    }  
    public void setFragmenterLength(int fragmenterLength) {  
        this.fragmenterLength = fragmenterLength;  
    }  
    public HighlighterParam(boolean highlight, String fieldName, String prefix, String stuffix, int fragmenterLength) {  
        this.highlight = highlight;  
        this.fieldName = fieldName;  
        this.prefix = prefix;  
        this.stuffix = stuffix;  
        this.fragmenterLength = fragmenterLength;  
    }  
      
    public HighlighterParam(boolean highlight, String fieldName, int fragmenterLength) {  
        this.highlight = highlight;  
        this.fieldName = fieldName;  
        this.fragmenterLength = fragmenterLength;  
    }  
      
    public HighlighterParam(boolean highlight, String fieldName, String prefix, String stuffix) {  
        this.highlight = highlight;  
        this.fieldName = fieldName;  
        this.prefix = prefix;  
        this.stuffix = stuffix;  
    }  
    public HighlighterParam() {  
    }  
}  