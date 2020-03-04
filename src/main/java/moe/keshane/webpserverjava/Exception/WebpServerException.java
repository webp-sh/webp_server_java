package moe.keshane.webpserverjava.Exception;

public class WebpServerException extends RuntimeException {
    public WebpServerException() {
        super();
    }

    public WebpServerException(String s) {
        super(s);
    }

    public WebpServerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public WebpServerException(Throwable throwable) {
        super(throwable);
    }

    protected WebpServerException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
