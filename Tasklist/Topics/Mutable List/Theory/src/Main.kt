// You can experiment here, it won’t be checked

fun main(args: Array<String>) {
  // put your code here
    val index: Int = readln().toInt()
    val mutList = mutableListOf<String>("")
    for (i in 0 .. args.size) if (i != index) mutList.add(args[i])
    println(mutList)
}
