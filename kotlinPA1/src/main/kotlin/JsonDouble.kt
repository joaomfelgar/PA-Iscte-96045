
class JsonDouble (val d:Double):JSonValue(){

    override fun serialize():String{
        return d.toString()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}