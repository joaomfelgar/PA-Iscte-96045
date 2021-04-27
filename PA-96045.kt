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
    fun visit (s:JsonString):Boolean=true
    fun visit (i:JsonInt):Boolean=true
    fun visit (b:JsonBoolean):Boolean=true
    fun visit (a:JsonArray):Boolean=true
    fun visit (mjo:MyJsonObject):Boolean=true
    fun visit (f:JsonFloat):Boolean=true
    fun visit (d:JsonDouble):Boolean=true
    fun visit (c:JsonChar):Boolean=true

    fun visit (n:JsonNull):Boolean=true

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


        override fun visit(n:JsonNull):Boolean{
            n.serialize()
            return true
        }





    }

    jsonarray.accept(v)





}


