package classes;

public class Variables {
    private static volatile Variables instance;
    private String data;

    private Variables(String data){
        this.data = data;
    }

    public static Variables getInstance(String data){
        Variables result = instance;
        if (result == null) {
            synchronized (Variables.class) {
                if (instance == null) {
                    instance = new Variables(data);
                }
            }
        }
        return result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;

    }
}
