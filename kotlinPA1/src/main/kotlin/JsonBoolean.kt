class JsonBoolean(b:Boolean): JSonValue(){

    val valor=b

    override fun serialize(): String{
        if(valor){
            return "true"
        }
        return "false"
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }

}