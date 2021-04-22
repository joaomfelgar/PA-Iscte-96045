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
    fun testSearchFor(){
        testerArray.addElement(jsonstring)
        testerArray.addElement(jsonstring2)
        testerArray.addElement(jsonint)



        var jsonarray2 = JsonArray()

        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring)
        jsonarray2.addElement(jsonstring2)
        jsonarray2.addElement(jsonint)

        testerArray.addElement(jsonarray2)

        var Exist = JsonString("OI")
        var NotExist= JsonString("NAO")


        Assert.assertTrue(testerArray.searchFor(Exist) == true)
        Assert.assertTrue(testerArray.searchFor(NotExist) == false)

    }
}