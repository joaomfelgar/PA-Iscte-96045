import org.junit.Test
import javax.print.DocFlavor
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class JsonClass(
    val name:String

)
@Target(AnnotationTarget.FIELD)
annotation class VALUE(

)

@Target(AnnotationTarget.FUNCTION)
annotation class FUN_NAME(

)



abstract class JSonValue{
    abstract fun serialize(): String
    abstract fun accept (v:Visitor)

}
@JsonClass("STRING")
class JsonString(s:String): JSonValue(){
    @VALUE
    val valor=s

    @FUN_NAME
    override fun serialize(): String {
        return "\""+valor.toString()+"\""
    }
    @FUN_NAME
    override fun accept(v: Visitor) {
        v.visit(this)
    }

}
@JsonClass("INT")
class JsonInt(i:Int): JSonValue(){
    @VALUE
    val valor=i


    @FUN_NAME
    override fun serialize(): String {
        return valor.toString()
    }
    @FUN_NAME
    override fun accept(v: Visitor) {
        v.visit(this)
    }


}

class JsonFloat (val f:Float):JSonValue(){
    @FUN_NAME
    override fun serialize():String{
        return f.toString()
    }
    @FUN_NAME
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class JsonDouble (val d:Double):JSonValue(){
    @FUN_NAME
    override fun serialize():String{
        return d.toString()
    }
    @FUN_NAME
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class JsonChar(val c: Char): JSonValue(){
    @FUN_NAME
    override fun serialize(): String {
       return "\'"+c.toString()+"\'"
    }
    @FUN_NAME
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class JsonNull(): JSonValue(){
    @VALUE
    val n= null
    @FUN_NAME
    override fun serialize():String{
        return "NULL"
    }
    @FUN_NAME
    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

/*
class JsonEnum(val en:Any):JSonValue(){


}

 */




class JsonBoolean(b:Boolean): JSonValue(){
    @VALUE
    val valor=b
    @FUN_NAME
    override fun serialize(): String{
        if(valor){
            return "true"
        }
        return "false"
    }
    @FUN_NAME
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
@JsonClass("ARRAY")
class JsonArray : JSonValue() {
    @VALUE
    val list: MutableList<JSonValue> = mutableListOf()
    @FUN_NAME
    fun addElement(value: JSonValue) {
        list.add(value)
    }
    @FUN_NAME
    fun contains(e: JSonValue): Boolean {
        return e in list

    }
    @FUN_NAME
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

    @FUN_NAME
    fun searchFor(exp:(JSonValue)->Boolean): List<String>{
    var final= mutableListOf<String>()
        var r= mutableListOf<String>()
       for (i in list){


            if ((i is JsonArray)==false) {



                    if(exp(i)==true) {
                        final.add(i.serialize())

                    }

            }
            else {
                 r= i.searchFor(exp) as MutableList<String>
            }

        }


    return final+r

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
    fun visit (jp:JsonPair):Boolean=true
    fun visit (n:JsonNull):Boolean=true
    //fun visit (directoryElement: DirectoryElement):Boolean=true
}

class JsonPair(val a: String, val b:JSonValue) :JSonValue() {

   override fun accept(v: Visitor) {
       v.visit(this)
   }

    override fun serialize(): String {
        return b.serialize()
    }



}
@JsonClass("JSONOBJECT")
class MyJsonObject() :JSonValue() {

    @VALUE
    val newLista = mutableListOf<JsonPair>()
    //val map : MutableMap<String,JSonValue> = mutableMapOf()

    @FUN_NAME
    fun addField(s: String, j: JSonValue) {
        //map[s]=j
        newLista.add(JsonPair(s, j))
    }
    @FUN_NAME
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
            "\"" + nova.declaredMemberProperties.joinToString(separator = ",") { it.name }


     */
        return "{\n " + resultado + " \n}"

    }
    @FUN_NAME
        override fun accept(v: Visitor) {

        v.visit(this)
        newLista.forEach { it.accept(v) }
    }


            /*
        v.visit(this)
       map.forEach{
           accept(v)
       }

        }

         */

    @FUN_NAME
    fun searchFor(exp:(JSonValue)->Boolean): List<String> {
        var final = mutableListOf<String>()
        var r= mutableListOf<String>()

        newLista.forEach {
            if(it.b is JsonArray == false){
                if(exp(it.b)){
                    final.add(it.b.serialize())


                }
            }
            else{
                r= it.b.searchFor(exp) as MutableList<String>

            }
        }
        return final+r
/*
        for (i in newLista){


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
        */





    }



}






fun main(){
    var jsonstring= JsonString("OI")
    var jsonstring2= JsonString("OI2")
    var jsonint = JsonInt(2)
    var jsonDouble=JsonDouble(2.2)



    var jsonarray = JsonArray()

    jsonarray.addElement(jsonstring)

    jsonarray.addElement(jsonstring)
    jsonarray.addElement(jsonstring2)
    jsonarray.addElement(jsonstring)
    jsonarray.addElement(jsonDouble)





    var jsonarray2 = JsonArray()
    jsonarray2.addElement(jsonstring)
    jsonarray2.addElement(jsonstring)
    jsonarray2.addElement(jsonstring2)
    jsonarray2.addElement(jsonint)

    jsonarray.addElement(jsonarray2)

    val limite = {s:JSonValue -> s is JsonInt }

    var new= jsonarray.searchFor(limite)

    println(new)


    var newjsononbject = MyJsonObject()
    newjsononbject.addField("Palavra", jsonstring)
    newjsononbject.addField("Palavra", jsonint)
    newjsononbject.addField("Palavra", jsonarray)

    var jsonString3= JsonString("OI")
    var clazz :KClass<Any> = jsonString3::class as KClass<Any>




    println(clazz.declaredMemberFunctions.joinToString(","){it.findAnnotation<FUN_NAME>().toString()})
    println(clazz.declaredMemberProperties.joinToString(","){it.hasAnnotation<VALUE>().toString()})


    println(newjsononbject.serialize())
    println(newjsononbject.searchFor(limite))




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
        /*
        override fun visit(mjo:MyJsonObject):Boolean{
            mjo.serialize()
            return true
        }

         */

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
        override fun visit(jp:JsonPair):Boolean{
            jp.serialize()
            return true
        }

        override fun visit(n:JsonNull):Boolean{
            n.serialize()
            return true
        }





    }

    jsonarray.accept(v)





}


