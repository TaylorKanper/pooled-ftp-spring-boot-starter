package com.kanper.service;

import com.kanper.conf.FtpConfig;
import com.kanper.factory.FtpClientFactory;
import com.kanper.pool.FtpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kangpeng
 */
@Slf4j
public class FtpService {
    private FtpClientPool pool;

    public FtpService(FtpConfig properties) throws Exception {
        FtpClientPool pool = new FtpClientPool(new FtpClientFactory(properties));
        this.pool = pool;
    }

    /**
     * 下载文件
     */
    public void download(String remote, String local) {
        FTPClient ftpClient = null;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            ftpClient = pool.borrowObject();
            File file = new File(local);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fileOutputStream = new FileOutputStream(file);
            inputStream = ftpClient.retrieveFileStream(remote);
            int size;
            byte[] bytes = new byte[ftpClient.getBufferSize()];
            while ((size = inputStream.read(bytes)) > 0) {
                fileOutputStream.write(bytes, 0, size);
                fileOutputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.completePendingCommand();
                if (ftpClient != null) {
                    pool.returnObject(ftpClient);
                }
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
