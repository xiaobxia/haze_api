package com.vxianjin.gringotts.pay.task;

import com.vxianjin.gringotts.pay.dao.UserQuotaApplyLogMapper;
import com.vxianjin.gringotts.pay.model.UserQuotaApplyLog;
import com.vxianjin.gringotts.pay.service.UserQuotaLogService;
import com.vxianjin.gringotts.pay.service.UserQuotaSnapshotService;
import com.vxianjin.gringotts.util.TimeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author jintian
 * @date 2018/9/20 14:11
 */
@Component
public class UserQuotaSynTask {

    @Autowired
    private UserQuotaSnapshotService userQuotaSnapshotService;

    private Executor userQuotaSynExecutor = Executors.newFixedThreadPool(1);

    private Executor everyDayuserQuotaExecutor = Executors.newFixedThreadPool(1);

    @Autowired
    private UserQuotaApplyLogMapper quotaApplyLogMapper;

    public void userQuotaSyn() {
        userQuotaSynExecutor.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                TimeKey.clear();
                TimeKey.start();
                List<UserQuotaApplyLog> userQuotaApplyLogs = quotaApplyLogMapper.queryFail();
                for (UserQuotaApplyLog userQuotaApplyLog : userQuotaApplyLogs) {
                    userQuotaSnapshotService.updateUserQuotaSnapshots(userQuotaApplyLog.getUserId(),userQuotaApplyLog.getId());
                    // 更新成已再次同步
                    quotaApplyLogMapper.updateToSended(userQuotaApplyLog.getId(),userQuotaApplyLog.getErrorNum() + 1);
                }
                TimeKey.clear();
            }
        }));

    }

    public void everyDayuserQuota() {
        everyDayuserQuotaExecutor.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                TimeKey.clear();
                TimeKey.start();
                List<UserQuotaApplyLog> userQuotaApplyLogs = quotaApplyLogMapper.queryTodayFail();
                for (UserQuotaApplyLog userQuotaApplyLog : userQuotaApplyLogs) {
                    userQuotaSnapshotService.updateUserQuotaSnapshots(userQuotaApplyLog.getUserId(),userQuotaApplyLog.getId());
                    // 更新成已再次同步
                    quotaApplyLogMapper.updateToSended(userQuotaApplyLog.getId(),userQuotaApplyLog.getErrorNum() + 1);
                }
                TimeKey.clear();
            }
        }));
    }
}
