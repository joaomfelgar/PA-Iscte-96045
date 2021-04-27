class MyJsonObject() :JSonValue() {



    val map : MutableMap<String,JSonValue> = mutableMapOf()


    fun addField(s: String, j: JSonValue) {
        map[s]=j

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




        return "{\n " + resultado + " \n}"

    }

    override fun accept(v: Visitor) {

        v.visit(this)
        for((x,y) in map){
            accept(v)
        }
    }




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


        return final+r









    }



}