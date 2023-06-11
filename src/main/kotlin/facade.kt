fun userFinder(login: String, userRepository: UserRepository) = userRepository.find(login)

fun user(login: String) = userFinder(login, UserRepositoryImpl) // only this should be exposed outside the module