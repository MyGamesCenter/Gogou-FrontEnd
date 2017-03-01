package info.gogou.gogou.request;

import org.springframework.core.io.ByteArrayResource;

import java.io.File;

/**
 * Created by lxu on 2016-06-09.
 */
public class ByteArrayFilenameResource extends ByteArrayResource {

    private File mFile;

    public ByteArrayFilenameResource(byte[] byteArray, String path) {
        super(byteArray);
        mFile = new File(path);
    }

    public String getFilename() {
        return mFile.getName();
    }
}
