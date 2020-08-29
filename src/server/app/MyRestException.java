package app;

public class MyRestException extends RuntimeException {

    private int httpStatuscode;

    public MyRestException(int httpStatuscode, String msg) {
        super(msg);
        this.httpStatuscode = httpStatuscode;
    }

    public int getHttpStatuscode() {
        return httpStatuscode;
    }
}
