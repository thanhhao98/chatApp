package com.muc;

/**
 *
 * @author Thanhhao
 */


import java.io.IOException;

public interface FileListener {
    void onRevFile(String revFrom,String nameFile,int sizeFile) throws IOException;
}
