class JsonFloat (val f:Float):JSonValue(){

    override fun serialize():String{
        return f.toString()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}