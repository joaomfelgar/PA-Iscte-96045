abstract class JSonValue{
    abstract fun serialize(): String
    abstract fun accept (v:Visitor)

}