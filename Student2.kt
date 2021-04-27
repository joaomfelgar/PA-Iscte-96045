
data class Student2(
    @Ignore
    val name: String,
    val age: Int,
    @ChangeName("Curso")
    val course: String




)