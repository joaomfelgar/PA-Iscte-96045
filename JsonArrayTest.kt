import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class JsonArrayTest : TestCase(){
    private var testerArray = JsonArray()
    private var jsonstring= JsonString("OI")
    private var jsonstring2= JsonString("OI2")
    private var jsonint = JsonInt(2)





    @Test
    fun testSerialize(){
        testerArray.addElement(jsonstring)
        testerArray.addElement(jsonstring2)
        testerArray.addElement(jsonint)



        var jsonarray2 = JsonArray()

        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring2)
        jsonarray2.addElement(jsonint)

        testerArray.addElement(jsonarray2)

        var compare = "[ \"OI\", \"OI2\", 2, [ \"OI\", \"OI\", \"OI2\", 2 ] ]"


        Assert.assertTrue(testerArray.serialize() == compare)



    }

    @Test
    fun testSearchFor(){/*
        testerArray.addElement(jsonstring)
        testerArray.addElement(jsonstring2)
        testerArray.addElement(jsonint)
        */



        var jsonarray2 = JsonArray()

        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring2)
        jsonarray2.addElement(jsonint)

        testerArray.addElement(jsonarray2)

        var Exist = JsonString("OI")
        var NotExist= JsonString("NAO")

        val limite = {s:JSonValue -> s.serialize().length>=2 } //No caso de Strings as aspas contam para o tamanho da string -> "OI"->4 outros objetos não -> "2"->1
        val limite2 = {s:JSonValue -> s.serialize().length>=8 }


        Assert.assertTrue(testerArray.searchFor(Exist,limite) == true)

        Assert.assertTrue(testerArray.searchFor(NotExist,limite) == false)

        Assert.assertTrue(testerArray.searchFor(Exist,limite2) == false)

        Assert.assertTrue(testerArray.searchFor(NotExist,limite2) == false)

    }
}