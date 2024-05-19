package com.soc.network.counter.service

import com.soc.network.counter.dao.CounterDao
import com.soc.network.counter.dao.CounterTxLogDao
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CounterService(
    private val counterDao: CounterDao,
    private val counterTxLogDao: CounterTxLogDao,
) {

    @Transactional
    fun increase(userUuid: UUID, count: Int, transactionUuid: UUID) {
        counterTxLogDao.createCountTx(transactionUuid, count)
        if (counterDao.getUserUnreadCount(userUuid) == null) {
            counterDao.createUserUnreadCount(userUuid, count, transactionUuid)
        } else {
            counterDao.updateUserUnreadCount(userUuid, count, transactionUuid)
        }
    }

    @Transactional
    fun reset(userUuid: UUID, transactionUuid: UUID) {
        val count = counterDao.getUserUnreadCount(userUuid)
        if (count != null) {
            counterTxLogDao.createCountTx(transactionUuid, -count)
            counterDao.updateUserUnreadCount(userUuid, -count, transactionUuid)
        }
    }

    @Transactional
    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 100))
    fun rollback(userUuid: UUID, transactionUuid: UUID) {
        counterDao.rollbackUserUnreadCount(userUuid, transactionUuid)
        counterTxLogDao.markUnreadCountTxRolledBack(transactionUuid)
    }

    fun get(userUuid: UUID): Int {
        return counterDao.getUserUnreadCountRo(userUuid) ?: 0
    }
}
