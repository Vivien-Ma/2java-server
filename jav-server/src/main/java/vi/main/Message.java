package vi.main;
import java.io.Serializable;
import java.util.Map;

public class Message implements Serializable {
    private Map<String, Object> data;

    public Message(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}