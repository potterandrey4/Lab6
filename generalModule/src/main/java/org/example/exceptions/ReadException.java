package generalModule.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadException extends FileNotFoundException {
    public ReadException() {
        super("Не удалось прочитать файл");
    }
}
