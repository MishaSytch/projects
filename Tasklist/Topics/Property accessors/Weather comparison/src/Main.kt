
data class City(val name: String) {
    var degrees: Int = 0
        set(value) {
            field = value
            if (field > 57 || field < -92) {
                when (name) {
                    "Moscow" -> field = 5
                    "Dubai" -> field = 30
                    "Hanoi" -> field = 20
                }
            }
        }

}        

fun main() {
    val first = readLine()!!.toInt()
    val second = readLine()!!.toInt()
    val third = readLine()!!.toInt()
    val firstCity = City("Dubai")
    firstCity.degrees = first
    val secondCity = City("Moscow")
    secondCity.degrees = second
    val thirdCity = City("Hanoi")
    thirdCity.degrees = third

    //implement comparing here
    val citys: List<City> = listOf(firstCity, secondCity, thirdCity)
    var min = citys.first().degrees
    val name = mutableListOf<String>()
    for (city in citys) {
        if (city.degrees < min) {
            name.clear()
            name.add(city.name)
            min = city.degrees
            continue
        }
        if (city.degrees == min) name.add(city.name)
    }
    if (name.size != 1) println("neither")
    else println(name.joinToString())
}