import org.junit.Test
import javax.print.DocFlavor
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class MyJsonString(
    val name:String

)
@Target(AnnotationTarget.PROPERTY)
annotation class VALUE(

)



abstract class JSonValue{
    abstract fun serialize(): String
    abstract fun accept (v:Visitor)

}
@MyJsonString("STRING")
class JsonString(s:String): JSonValue(){
    @VALUE
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


    fun searchFor(e:JSonValue, exp:(JSonValue)->Boolean): Boolean{

       for (i in list){


            if ((i is JsonArray)==false) {



                    if(exp(i)==true) {
                        if (i.serialize() == e.serialize()) {

                            return true
                        }
                    }

            }
            else {
                return i.searchFor(e,exp)
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
    fun visit (a:String, b: JSonValue):Boolean=true
    //fun visit (directoryElement: DirectoryElement):Boolean=true
}

class JsonPair(val a: String, val b:JSonValue) {
/*
   override fun accept(v: Visitor) {
       v.visit(this)
   }

    override fun serialize(): String {
        return a.toString() + ": " + b.toString()
    }
*/


}
class MyJsonObject(): JSonValue() {
    val newLista = mutableListOf<JsonPair>()
    //val map : MutableMap<String,JSonValue> = mutableMapOf()


    fun addField(s: String, j: JSonValue) {
        //map[s]=j
        newLista.add(JsonPair(s, j))
    }

    override fun serialize(): String {

        var resultado = ""
        /*
        for((k,v) in map){
            resultado=resultado + k.toString() +": "+ v.serialize() + "\n"
        }
        return "{ " +"\n" + resultado + "\n" + "}"

*/

        newLista.forEach {
            if (newLista.indexOf(it) == (newLista.size - 1)) {
                resultado = resultado + "\n " + "\"" + it.a + "\"" + ": " + it.b.serialize() + "\n"
            } else {


                resultado = resultado + "\n " + "\"" + it.a + "\"" + ": " + it.b.serialize() + " , \n"
            }

        }

    /*
        var nova : KClass<Any> = newLista::class as KClass<Any>

        resultado =
            "\"" + nova.findAnnotation<MyJsonString>()!!.name + "\": " + nova.declaredMemberProperties.joinToString(separator = ",") { if (it.hasAnnotation<VALUE>()){it.name} else{ ""} }
        */
        return "{\n " + resultado + " \n}"

    }

        override fun accept(v: Visitor) {
            /*
        v.visit(this)
        newLista.forEach { it.accept(v) }
    }

         */
            /*
        v.visit(this)
       map.forEach{
           accept(v)
       }

        }

         */
        }



}






fun main(){
    var jsonstring= JsonString("OI")
    var jsonstring2= JsonString("OI2")
    var jsonint = JsonInt(2)



    var jsonarray = JsonArray()

    jsonarray.addElement(jsonstring)

    jsonarray.addElement(jsonstring)
    jsonarray.addElement(jsonstring2)
    jsonarray.addElement(jsonstring)





    var jsonarray2 = JsonArray()
    jsonarray2.addElement(jsonstring)
    jsonarray2.addElement(jsonstring)
    jsonarray2.addElement(jsonstring2)
    jsonarray2.addElement(jsonint)

    jsonarray.addElement(jsonarray2)

    val limite = {s:JSonValue -> s.serialize().length >= 1 }

    var new= jsonarray.searchFor(jsonint,limite)
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


