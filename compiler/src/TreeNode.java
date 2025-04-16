import java.util.ArrayList;
import java.util.List;

class TreeNode {
    public int data;
    public int p;
    public int line;
    public int tableId;
    public String type;
    public String content;
    public String regId="";//地址寄存器
    public String value="";//值寄存器
    public String returnType="";//返回值类型(函数时使用)
    public TreeNode parent; // 对父节点的引用
    public ArrayList<TreeNode> children  = new ArrayList<TreeNode>();

    public int level=0;
    public int stmtId =0;
    public int yesId =0;
    public int noId =0;
    public int breakId =0;
    public int continueId =0;
    public ValueDetail key = new ValueDetail();


    public void setYesId(int yesId) {
        this.yesId = yesId;
    }

    public void setNoId(int noId) {
        this.noId = noId;
    }

    public void setStmtId(int stmtId) {
        this.stmtId = stmtId;
    }

    public int getStmtId() {
        return stmtId;
    }

    public void setKey(ValueDetail key) {
        this.key = key;
    }

    public ValueDetail getKey() {
        return key;
    }
    public String getValue(){
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegId() {
        return regId;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBreakId() {
        return breakId;
    }

    public void setBreakId(int breakId) {
        this.breakId = breakId;
    }

    public int getContinueId() {
        return continueId;
    }

    public void setContinueId(int continueId) {
        this.continueId = continueId;
    }

    public TreeNode(int data) {
        this.data = data;
        this.children = new ArrayList<>();
    }
    public ArrayList<Integer> FuncFParams ;
    public ArrayList<String> FuncFParamsName ;

    public ArrayList<Integer> FuncRParamsType = new ArrayList<>();//0 -> a, 1 -> a[], 2 -> a[][],
    public ArrayList<String> FuncRParamsName;
    //只对函数标识符有效
    public int isInt;
    public int expType = 0;//0 -> a, 1 -> a[], 2 -> a[][],// -1 -> func
    public void addChild(TreeNode child) {
        child.parent = this; // 在添加子节点时设置父节点引用
        children.add(child);
    }
    public String getContent(){
        return content;
    }
    public String getType() {
        return type;
    }


    public List<TreeNode> getChildren() {
        return children;
    }
    public TreeNode() {
        // 这是无参数构造函数的主体，可以为空，或者进行一些初始化操作
    }
    public TreeNode(int p,int tableId,String type){
        this.p = p;
        this.tableId = tableId;
        this.type = type;
        this.content = "";
    }
    public TreeNode(int p,int tableId,String type,String content){
        this.p = p;
        this.tableId = tableId;
        this.type = type;
        this.content = content;
    }
    @Override
    public String toString() {
        return "TreeNode "+type+" "+content+" "+expType;
    }

}