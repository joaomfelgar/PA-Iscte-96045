import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class MyJsonObjectTest : TestCase(){

    private var jsonstring= JsonString("OI")
    private  var jsonstring2= JsonString("OI2")
    private  var jsonint = JsonInt(2)



    private var jsonarray = JsonArray()
    private var jsonarray2 = JsonArray()
    private var newjsononbject = MyJsonObject()


    @Test
    fun testMyJsonObject() {

        jsonarray.addElement(jsonstring)

        jsonarray.addElement(jsonstring)
        jsonarray.addElement(jsonstring2)
        jsonarray.addElement(jsonstring)






        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring2)
        jsonarray2.addElement(jsonint)

        jsonarray.addElement(jsonarray2)





        newjsononbject.addField("Palavra", jsonstring)
        newjsononbject.addField("Palavra", jsonint)
        newjsononbject.addField("Palavra", jsonarray)


        /*var compare="{\n" +
                " \n" +
                " \"Palavra\": \"OI\" , \n" +
                "\n" +
                " \"Palavra\": 2 , \n" +
                "\n" +
                " \"Palavra\": [ \"OI\", \"OI\", \"OI2\", \"OI\", [ \"OI\", \"OI\", \"OI2\", 2 ] ]\n" +
                " \n" +
                "}"
                */


        //Assert.assertTrue(newjsononbject.serialize() == compare)

        var teste3 = Student2("João",21,"MEI")

        var compare2= "{ \n"+
            "\n"+
            "\"age\": 21" + " , \n"+
                "\n"+
            "\"Curso\": \"MEI\"" +"\n"+

        "\n"+

        "}"

        //println(autoJson(teste3).serialize())
        //println(compare2)

        assertEquals(autoJson(teste3).serialize(),compare2)





    }
}
