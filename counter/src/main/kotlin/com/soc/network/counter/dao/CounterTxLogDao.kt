package com.soc.network.counter.dao

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*

@Service
class CounterTxLogDao(
    private val jdbcTemplate: JdbcTemplate
) {
    private val createTxSql = "insert into counter_tx_log(tx_uuid, count) values(?,?)"
    private val markTxRolledBackSql = "update counter_tx_log set rolled_back=true where tx_uuid=?"
    private val deleteExpiredTxsSql = "delete from counter_tx_log where toDate(tx_dt)<(now()-interval '1 minute')"

    @Scheduled(cron = "0 * * * * *")
    fun deleteExpiredUnreadCountTxs() {
        jdbcTemplate.update(deleteExpiredTxsSql)
    }

    fun createCountTx(transactionUuid: UUID, count: Int) {
        jdbcTemplate.update(createTxSql) { ps ->
            ps.setObject(1, transactionUuid)
            ps.setInt(2, count)
        }
    }

    fun markUnreadCountTxRolledBack(transactionUuid: UUID) {
        jdbcTemplate.update(markTxRolledBackSql) { ps ->
            ps.setObject(1, transactionUuid)
        }
    }
}