class JsonNull(): JSonValue(){

    val n= null

    override fun serialize():String{
        return "NULL"
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}