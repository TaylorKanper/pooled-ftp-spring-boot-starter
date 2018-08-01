package com.kanper.factory;

import com.kanper.conf.FtpConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

/**
 * @author kangpeng
 */
@Slf4j
public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {
    private FtpConfig config;

    public FtpClientFactory(FtpConfig ftpConfig) {
        this.config = ftpConfig;
    }

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(config.getEncoding());
        ftpClient.setConnectTimeout(config.getConnectTimeout());
        try {

            ftpClient.connect(config.getHost(), config.getPort());
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                log.warn("FTPServer refused connection,replyCode:{}", replyCode);
                return null;
            }

            if (!ftpClient.login(config.getUserName(), config.getPassword())) {
                log.warn("ftpClient login failed... username is {}; password: {}", config.getUserName(), config.getPassword());
            }

            ftpClient.setBufferSize(config.getBufferSize());
            ftpClient.setFileType(config.getTransferFileType());
            if (config.isPassiveMode()) {
                ftpClient.enterLocalPassiveMode();
            }

        } catch (IOException e) {
            log.error("create ftp connection failed...", e);
        }
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient obj) {
        return new DefaultPooledObject<>(obj);
    }

    /**
     * 销毁FtpClient对象
     */
    @Override
    public void destroyObject(PooledObject<FTPClient> ftpPooled) {
        if (ftpPooled == null) {
            return;
        }

        FTPClient ftpClient = ftpPooled.getObject();

        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException io) {
            log.error("ftp client logout failed...{}", io);
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException io) {
                log.error("close ftp client failed...{}", io);
            }
        }
    }

    /**
     * 验证FtpClient对象
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> ftpPooled) {
        try {
            FTPClient ftpClient = ftpPooled.getObject();
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error("Failed to validate client: {}", e);
        }
        return false;
    }

}
