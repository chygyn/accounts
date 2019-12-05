package awesome.account.app.accounts.repository

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.LockModeType

@Repository
interface AccountRepository : CrudRepository<Account, Long> {

    @Modifying(clearAutomatically = true)
    @Query(
        """update Account a 
        set a.balance = :newBalance
        where a.id = :id
    """
    )
    fun updateAccountAmount(
        @Param("id") id: Long, @Param("newBalance") newBalance: Long
    ): Int

    @Query("""select a from Account a where a.id=:id""")
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun findOneByIdForUpdate(@Param("id") id: Long): Optional<Account>

}