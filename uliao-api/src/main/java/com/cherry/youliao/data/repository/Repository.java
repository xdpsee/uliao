package com.cherry.youliao.data.repository;


import com.zaxxer.hikari.HikariDataSource;
import org.nutz.dao.ConnCallback;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class Repository extends NutDao implements Dao, InitializingBean {

    @Autowired
    HikariDataSource dataSource;

    @Override
    public void afterPropertiesSet() throws Exception {
        setDataSource(dataSource, false);
    }

    public void run(ConnCallback callback) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            callback.invoke(con);
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            else
                throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

}