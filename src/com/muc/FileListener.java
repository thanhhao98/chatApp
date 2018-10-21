package com.muc;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileListener {
    public void onRevFile(String revFrom,String nameFile,int sizeFile) throws IOException;
}
