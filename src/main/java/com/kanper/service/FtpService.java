package com.kanper.service;

import com.kanper.conf.FtpConfig;
import com.kanper.factory.FtpClientFactory;
import com.kanper.pool.FtpClientPool;

/**
 * @author kangpeng
 */
public class FtpService {
    private FtpClientPool pool;

    public FtpService(FtpConfig properties) throws Exception {
        FtpClientPool pool = new FtpClientPool(new FtpClientFactory(properties));
        this.pool = pool;
    }

    public void download() throws Exception {
        System.out.println("进入download");
    }


}
