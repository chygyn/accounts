package awesome.account.app.accounts.repository

import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,

    @Column(name = "name")
    val name: String,

    @Column(name = "balance")
    var balance: Long = 0
)