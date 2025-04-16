package line;

public class Line {
    public int level;
    public String content;
    public Line(int level, String content){
        this.level = level;
        this.content = content;
    }
    @Override
    public String toString() {
        return content;
    }
}
