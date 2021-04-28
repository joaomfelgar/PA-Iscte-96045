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
annotation class Ignore

interface Visitor{
    fun visit (s:JsonString)
    fun visit (i:JsonInt)
    fun visit (b:JsonBoolean)
    fun visit (a:JsonArray):Boolean=true
    fun visit (mjo:MyJsonObject):Boolean=true
    fun visit (f:JsonFloat)
    fun visit (d:JsonDouble)
    fun visit (c:JsonChar)

    fun visit (n:JsonNull)

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
    else if(a is Enum<*>){
        finalJson=JsonString(a.toString())


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
enum class Teste(val x: Int){
    OI(3),
    XAU(2),
    oi(1)

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




    var newjsononbject = MyJsonObject()
    newjsononbject.addField("Palavra", jsonstring)
    newjsononbject.addField("Palavra", jsonint)
    newjsononbject.addField("Palavra", jsonarray)










    var teste= autoJson("OI")
    println(teste)


    var teste2 = Student2("João",21,"MEI")


    println(autoJson(teste2).serialize())
    var testar= Teste.OI.x
    println(autoJson(testar).serialize())





    val v = object:Visitor{
         override fun visit (s:JsonString){
            s.serialize()

        }

        override fun visit (i:JsonInt){
            i.serialize()

        }

        override fun visit (b:JsonBoolean){
            b.serialize()

        }

        override fun visit (a:JsonArray):Boolean{
            a.serialize()
            return true
        }

        override fun visit(mjo:MyJsonObject):Boolean{
            mjo.serialize()
            return true
        }


        override fun visit(f:JsonFloat){
            f.serialize()

        }
        override fun visit(d:JsonDouble){
            d.serialize()

        }
        override fun visit(c:JsonChar){
            c.serialize()

        }


        override fun visit(n:JsonNull){
            n.serialize()

        }





    }

    jsonarray.accept(v)





}


