import java.util.HashMap;
public class Table {
    public int id;
    public int fatherId;
    public int isFunction = 0;
    public int isInt;//1 int 0 void
    public HashMap<String,Symbol> symbolList = new HashMap<String, Symbol>();
    public Table(int id, int fatherId) {
        this.id = id;
        this.fatherId = fatherId;
        this.symbolList = new HashMap<>();
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }
    public HashMap<String,Symbol> getSymbolList() {
        return symbolList;
    }
    @Override
    public String toString() {
        return id +" "+ fatherId;
    }

}
