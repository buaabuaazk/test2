/**
 * @author zwzk8
 */
public class Token {
    private String content;
    private String type;
    private int lineNum;

    public int loopStart=0;
    public int loopEnd=0;
    public int isLoop=0;
    // 构造函数
    public Token(String content, String type, int lineNum) {
        this.content = content;
        this.type = type;
        this.lineNum = lineNum;
    }

    // 获取content属性的方法
    public String getContent() {
        return content;
    }

    // 设置content属性的方法
    public void setContent(String content) {
        this.content = content;
    }

    // 获取type属性的方法
    public String getType() {
        return type;
    }

    // 设置type属性的方法
    public void setType(String type) {
        this.type = type;
    }

    // 获取lineNum属性的方法
    public int getLineNum() {
        return lineNum;
    }

    // 设置lineNum属性的方法
    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    // 其他方法和逻辑可以在这里添加
    @Override
    public String toString() {
        if(content.equals("")){
            return type;
        }
        return type + " " + content;
    }
}

