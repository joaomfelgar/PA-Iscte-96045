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
        if (v.visit(this)) {
            list.forEach {
                it.accept(v)

            }
        }
    }



}