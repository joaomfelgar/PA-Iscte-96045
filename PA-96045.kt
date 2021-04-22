import org.junit.Test


abstract class JSonValue{
    abstract fun serialize(): String
    abstract fun accept (v:Visitor)

}
class JsonString(s:String): JSonValue(){

    val valor=s

    override fun serialize(): String {
        return "\""+valor.toString()+"\""
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }

}

class JsonInt(i:Int): JSonValue(){

    val valor=i

    override fun serialize(): String {
        return valor.toString()
    }
    override fun accept(v: Visitor) {
        v.visit(this)
    }


}

class JsonFloat (val f:Float):JSonValue(){
    override fun serialize():String{
        return f.toString()
    }
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class JsonDouble (val d:Double):JSonValue(){
    override fun serialize():String{
        return d.toString()
    }
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class JsonChar(val c: Char): JSonValue(){
    override fun serialize(): String {
       return "\'"+c.toString()+"\'"
    }
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

/*
class JsonEnum(val en:Enum):JSonValue(){

}
*/



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

/*
class JsonMap : JSonValue(){
    val Mymap: MutableMap<String, JSonValue> = mutableMapOf()

    fun addNew(s:String, js:JSonValue){
        Mymap[s]=js
    }

    override fun serialize(): String {
        var final=""
        for((k,v) in Mymap){
            final=final + k.toString() +": "+ v.serialize() + "\n"
        }
        return "{ " +"\n" + final + "\n" + "}"
    }
}

 */

class JsonArray : JSonValue() {
    val list: MutableList<JSonValue> = mutableListOf()

    fun addElement(value: JSonValue) {
        list.add(value)
    }

    fun contains(e: JSonValue): Boolean {
        return e in list

    }

    override fun serialize(): String {
        var lista2 = "["
        list.forEach {
            if (list.indexOf(it) == (list.size-1)) {
                lista2 = lista2 + " " + it.serialize() +" "
            }
            else {

                lista2 = lista2 + " " + it.serialize() + ","


            }


        }
        return lista2 + "]"
    }


    fun searchFor(e:JSonValue): Boolean{


        list.forEach {


            if (it is JsonArray==false) {

                if (it.serialize() == e.serialize()) {
                    println(it.serialize())
                    return true
                }

            }
            else {
                it.searchFor(e)


            }

        }

        return false

    }
    override fun accept(v: Visitor) {
        v.visit(this)
        list.forEach{
            it.accept(v)

        }
    }



}

interface Visitor{
    fun visit (s:JsonString):Boolean=true
    fun visit (i:JsonInt):Boolean=true
    fun visit (b:JsonBoolean):Boolean=true
    fun visit (a:JsonArray):Boolean=true
    fun visit (mjo:MyJsonObject):Boolean=true
    fun visit (f:JsonFloat):Boolean=true
    fun visit (d:JsonDouble):Boolean=true
    fun visit (c:JsonChar):Boolean=true
    //fun visit (directoryElement: DirectoryElement):Boolean=true
}

class JsonPair(val a: String, val b:JSonValue){
    /*
   fun accept(v: Visitor) {
       v.visit(this)
   }
   */



}
class MyJsonObject(): JSonValue() {
    val newLista = mutableListOf<JsonPair>()
    //val map : MutableMap<String,JSonValue> = mutableMapOf()


    fun addField(s:String, j: JSonValue){
        //map[s]=j
        newLista.add(JsonPair(s,j))
    }

    override fun serialize(): String {
        /*
        var final=""
        for((k,v) in map){
            final=final + k.toString() +": "+ v.serialize() + "\n"
        }
        return "{ " +"\n" + final + "\n" + "}"

    */

        var resultado=""
        newLista.forEach {
            if(newLista.indexOf(it)==(newLista.size-1)){
                resultado = resultado + "\n " + "\""+ it.a +"\"" + ": " + it.b.serialize() + "\n"
            }
            else {


                resultado = resultado + "\n " + "\"" + it.a + "\"" + ": " + it.b.serialize() + " , \n"
            }

        }

    return "{\n "+ resultado+ " \n}"
    }

    override fun accept(v: Visitor) {


        }
    }










fun main(){
    var jsonstring= JsonString("OI")
    var jsonstring2= JsonString("OI2")
    var jsonint = JsonInt(2)



    var jsonarray = JsonArray()
    /*
    jsonarray.addElement(jsonstring)

    jsonarray.addElement(jsonstring)
    jsonarray.addElement(jsonstring2)
    jsonarray.addElement(jsonstring)

     */



    var jsonarray2 = JsonArray()
    jsonarray2.addElement(jsonstring)
    jsonarray2.addElement(jsonstring)
    jsonarray2.addElement(jsonstring2)
    jsonarray2.addElement(jsonint)

    jsonarray.addElement(jsonarray2)

    var new= jsonarray.searchFor(jsonstring)
    println(jsonarray.serialize())
    println(new)


    var newjsononbject = MyJsonObject()
    newjsononbject.addField("Palavra", jsonstring)
    newjsononbject.addField("Palavra", jsonint)
    newjsononbject.addField("Palavra", jsonarray)

    println(newjsononbject.serialize())




    val v = object:Visitor{
         override fun visit (s:JsonString):Boolean{
            s.serialize()
            return true
        }

        override fun visit (i:JsonInt):Boolean{
            i.serialize()
            return true
        }

        override fun visit (b:JsonBoolean):Boolean{
            b.serialize()
            return true
        }

        override fun visit (a:JsonArray):Boolean{
            a.serialize()
            return true
        }
        override fun visit(mjo:MyJsonObject):Boolean{
            mjo.serialize()
            return true
        }

        override fun visit(f:JsonFloat):Boolean{
            f.serialize()
            return true
        }
        override fun visit(d:JsonDouble):Boolean{
            d.serialize()
            return true
        }
        override fun visit(c:JsonChar):Boolean{
            c.serialize()
            return true
        }


    }

    jsonarray.accept(v)





}


