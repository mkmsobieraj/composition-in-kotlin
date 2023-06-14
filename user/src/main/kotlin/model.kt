// I do not see idea of calling Facade a root (entry point) module class. In example bellow, let imagine, we expose only user function.
// Isn't it more intuitive and more in DDD approach than UserFacade to have all other staff as a part of User domain?

import ProductType.MORTGAGE
import ProductType.SHORT_TERM_LOAN
import RiskClass.LOW
import UserStatus.ACTIVE
import UserStatus.IN_ACTIVE
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.security.Timestamp

data class User(
    val login: String,
    val role: Role,
    val products: Products,
    private val userStatusService: UserStatusService,
    private val riskService: RiskService,
): UserStatusService by userStatusService, RiskService by riskService {
}

interface UserStatusService {
    val status: UserStatus
    fun convert(): UserStatus
}

interface RiskService {
    // one drawback is not so easy process of updating fields, so in some cases it is worth to consider to move only methods without fields
    val riskClass: RiskClass
    fun asses(): RiskClass
}

data class Role(
    val name: RoleName
)

// I criticized, but I find good reasoning for that. It gives good semantic meaning. For example, I can add business methods with very intuitive meaning
// user.products.loans (meaning for user products, get loans), or user.products.balance (get balance for all products)
// argument can be made that it should be a interface, but I do not see the necessity
data class Products(
    private val products: Set<Product>
) {
    fun loans() = products.filter { setOf(SHORT_TERM_LOAN, MORTGAGE).contains(it.type) }
    fun balance() = products.map { it.currentAmount }.fold(ZERO) { total, currentAmount -> total + currentAmount }
}

data class Product(
    val type: ProductType,
    val initialAmount: BigDecimal,
    val currentAmount: BigDecimal,
    val cashFlows: CashFlows,
)

data class CashFlows(val cashFlows: List<CashFlow>)

data class CashFlow(
    val amount: BigDecimal,
    val timestamp: Timestamp,
    val status: CashFlowStatus,
) {
}

enum class RoleName {
    PERSON,
    COMPANY,
}

enum class UserStatus {
    ACTIVE,
    IN_ACTIVE,
}

enum class RiskClass {
    UNKNOWN,
    LOW,
    MEDIUM,
    HEIGHT,
    FRAUD,
}

enum class ProductType {
    SHORT_TERM_LOAN,
    LEASING,
    MORTGAGE,
}


enum class CashFlowStatus {
    SUCCESSFUL,
    PENDING,
    ORDERED,
    FAILED
}

// we discuss creating interface and only one implementation of it on our training. What do you think about it?
internal class UserStatusServiceImpl: UserStatusService {
    override val status: UserStatus
        get() = ACTIVE // in reality, it would be acquired from somewhere, like external service or something
    override fun convert() = if (status == ACTIVE) IN_ACTIVE else ACTIVE

}

internal class RiskServiceImpl: RiskService {
    override val riskClass: RiskClass
        get() = LOW

    override fun asses() = RiskClass.values().random()

}