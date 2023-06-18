import RoleName.PERSON

internal interface UserRepository {
    fun find(login: String): User?
    fun add(user: User): User?
}

internal object UserRepositoryImpl: UserRepository {
    private val users: MutableMap<String, User> = mutableMapOf(
        "test" to User.create("test", Role(PERSON))
    )

    override fun find(login: String): User? = users[login]

    override fun add(user: User): User? = users.put(user.login, user)

}