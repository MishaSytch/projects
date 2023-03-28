data class Comment(val id: Int, val body: String, val author: String)

fun printComments(commentsData: MutableList<Comment>){
    // write you code here
    for (i in commentsData)
        println("Author: ${i.author}; Text: ${i.body}")
}