class JsonString(s:String): JSonValue(){

    val valor=s


    override fun serialize(): String {
        return "\""+valor.toString()+"\""
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }

}