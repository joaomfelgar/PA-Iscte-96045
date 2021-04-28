class JsonChar(val c: Char): JSonValue(){

    override fun serialize(): String {
        return "\'"+c.toString()+"\'"
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}