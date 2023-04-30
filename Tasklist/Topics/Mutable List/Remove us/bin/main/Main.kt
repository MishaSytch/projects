fun solution(elements: MutableList<String>, index: Int): MutableList<String> {
    val mutList = mutableListOf<String>()
    for (i in 0 until  elements.size) if (i != index) mutList.add(elements[i])
    return mutList
}