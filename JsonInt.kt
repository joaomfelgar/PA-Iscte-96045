class JsonInt(i:Int): JSonValue(){

    val valor=i



    override fun serialize(): String {
        return valor.toString()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }


}