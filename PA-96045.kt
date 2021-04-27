import org.junit.Test
import org.omg.CORBA.Object
import javax.print.DocFlavor
import kotlin.reflect.KClass
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class ChangeName(
    val name:String

)
@Target(AnnotationTarget.PROPERTY)
annotation class Ignore(

)





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

class JsonNull(): JSonValue(){

    val n= null

    override fun serialize():String{
        return "NULL"
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

/*
class JsonEnum(val en:Any):JSonValue(){


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

class MyJsonObject() :JSonValue() {


    //val newLista = mutableListOf<JsonPair>()
    val map : MutableMap<String,JSonValue> = mutableMapOf()


    fun addField(s: String, j: JSonValue) {
        map[s]=j
        //newLista.add(JsonPair(s, j))
    }

    override fun serialize(): String {

        var resultado = ""
        var count=1

        for((k,v) in map) {

            if (count < map.size) {

                resultado = resultado + "\n" + "\"" + k.toString() + "\"" + ": " + v.serialize() + " , \n"
                count++
            }
            else if (count==map.size){
                resultado = resultado + "\n" + "\"" + k.toString() + "\"" + ": " + v.serialize() + "\n"
            }
        }
        return "{ " +"\n" + resultado + "\n" + "}"


    /*
        newLista.forEach {
            if (newLista.indexOf(it) == (newLista.size - 1)) {
                resultado = resultado + "\n " + "\"" + it.a + "\"" + ": " + it.b.serialize() + "\n"
            } else {


                resultado = resultado + "\n " + "\"" + it.a + "\"" + ": " + it.b.serialize() + " , \n"
            }

        }


     */


    /*
        var nova : KClass<Any> = newLista::class as KClass<Any>

        resultado =
            "\"" + nova.declaredMemberProperties.joinToString(separator = ",") { it.name }


     */
        return "{\n " + resultado + " \n}"

    }

        override fun accept(v: Visitor) {

        v.visit(this)
        for((x,y) in map){
            accept(v)
        }
    }


            /*
        v.visit(this)
       map.forEach{
           accept(v)
       }

        }

         */


    fun searchFor(exp:(JSonValue)->Boolean): List<String> {
        var final = mutableListOf<String>()
        var r= mutableListOf<String>()

        for((k,v) in map) {
            if (v is JsonArray == false) {
                if (exp(v)) {
                    final.add(v.serialize())
                }
            }
            else{
                r=v.searchFor(exp) as MutableList<String>
            }
        }

        /*

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

         */
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

fun autoJson(a:Any?):JSonValue {
    var finalJson:JSonValue = JsonNull()
    if (a is String) {

        finalJson = JsonString(a)
    } else if (a is Int) {
         finalJson = JsonInt(a)
    } else if (a is Boolean) {
        finalJson = JsonBoolean(a)

    } else if (a is Double) {
        finalJson = JsonDouble(a)
    }
    else if (a is Float) {
        finalJson = JsonFloat(a)
    }
    else if (a is Char) {
        finalJson = JsonChar(a)
    }
    else if (a is Collection<*>) {
        finalJson = JsonArray()

        a.forEach {
            var novoJson= autoJson(it)
            (finalJson as JsonArray).addElement(novoJson)
        }
    }

    else if (a is Map<*,*>) {
        finalJson = MyJsonObject()
        for((k,v) in a){
            (finalJson).addField(k.toString(), autoJson(v))
        }
    }

    else if (a!!::class.isData){

        finalJson=MyJsonObject()

        var clazz :KClass<Any> = a::class as KClass<Any>
        clazz.declaredMemberProperties.forEach {
            if (it.hasAnnotation<ChangeName>()) {
                (finalJson).addField(it.findAnnotation<ChangeName>()!!.name, autoJson(it.call(a)))
            }
            else if(it.hasAnnotation<Ignore>()){

            }

            else {
                (finalJson).addField(it.name.toString(), autoJson(it.call(a)))
            }
        }
    }

return finalJson



}

data class Person(
    @Ignore
    val name: String,
    val age: Int,
    val height: Double,
    @ChangeName("PESO")
    val weight: Double,
    val isAlive: Boolean,
    val friends: MutableList<Person>
)


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







    println(newjsononbject.serialize())
    println(newjsononbject.searchFor(limite))

    var teste= autoJson("OI")
    println(teste)
    var teste2 = mutableListOf<Any>()
    teste2.add("oi")
    teste2.add("oi")
    teste2.add(2)

    println(autoJson(teste2).serialize())

    var teste3 = Person("João",21,1.72,71.3,true, mutableListOf<Person>())


    println(autoJson(teste3).serialize())



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


