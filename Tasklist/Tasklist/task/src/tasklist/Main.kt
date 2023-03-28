package tasklist

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.*
import java.io.File
import java.time.LocalTime

private val tasks = mutableListOf<Task>()
private data class Task(var priority: String, var date: LocalDate, var tag: Char = '!', var time: String, var task: MutableList<String>)
private val jsonFile = File("tasklist.json")
class Tmp(
    val priority: String,
    val date: String,
    val tag: String,
    val time: String,
    val task: List<String>
)
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val type = Types.newParameterizedType(List::class.java, Tmp::class.java)
private val taskAdapter = moshi.adapter<List<Tmp?>>(type)

fun main() {
    if (jsonFile.exists()) fromJSON()
    while (firstCommand()) {}
}

private fun firstCommand(): Boolean {
    println("Input an action (add, print, edit, delete, end):")
    when (readln().trim().lowercase()) {
        "add" -> {
            gettingTasks()
            println()
        }
        "end" -> {
            if (!tasks.isEmpty()) save()
            println("Tasklist exiting!")
            return false
        }
        "print" -> {
            if (tasks.isEmpty()) println("No tasks have been input")
            else printTasks()
            println()
        }
        "edit" -> {
            if (tasks.isEmpty()) println("No tasks have been input")
            else {
                printTasks()
                println()
                edit()
            }
        }
        "delete" -> {
            if (tasks.isEmpty()) println("No tasks have been input")
            else {
                printTasks()
                println()
                delete()
            }
        }
        else -> println("The input action is invalid")
    }
    return true
}

private fun gettingTasks() {
    val task = Task(priority = receivingPriority(), date = receivingDate(), time = receivingTime(), task = receivingListOfTask())
    task.tag = receivingTag(task.date)
    tasks.add(task)
}

private fun receivingPriority(): String {
    var priority: String
    while (true) {
        println("Input the task priority (C, H, N, L):")
        priority = readln().trim()
        when (priority.lowercase()) {
            "c" -> break
            "h" -> break
            "n" -> break
            "l" -> break
        }
    }
    return priority
}

private fun receivingDate(): LocalDate {
    while (true) {
        println("Input the date (yyyy-mm-dd):")
        try {
            var date1 = readln().trim()
            if (date1.split("-")[0].length == 4 && (date1.split("-")[1].length == 2 || date1.split("-")[1].length == 1) && (date1.split("-")[2].length == 2 || date1.split("-")[2].length == 1)){
                if (date1.split("-")[1].length == 1) date1 = "${date1.split("-")[0]}-0${date1.split("-")[1]}-${date1.split("-")[2]}"
                if (date1.split("-")[2].length == 1) date1 = "${date1.split("-")[0]}-${date1.split("-")[1]}-0${date1.split("-")[2]}"
                return LocalDate.parse(date1)
            }
            else {
                println("The input date is invalid")
                continue
            }
        } catch (e: Exception) {
            println("The input date is invalid")
            continue
        }
    }
}

private fun receivingTag(date: LocalDate): Char {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
    if (date.daysUntil(currentDate) == 0) return 'T'
    if (date.daysUntil(currentDate) < 0) return 'I'
    return 'O'
}

private fun receivingTime(): String {
    while (true) {
        println("Input the time (hh:mm):")
        try {
            var time1 = readln().trim()
            if ((time1.split(":")[0].length == 2 || time1.split(":")[0].length == 1) && (time1.split(":")[1].length == 2 || time1.split(":")[1].length == 1)) {
                if (time1.split(":")[0].length == 1) time1 = "0${time1.split(":")[0]}:${time1.split(":")[1]}"
                if (time1.split(":")[1].length == 1) time1 = "${time1.split(":")[0]}:0${time1.split(":")[1]}"
                return LocalTime.parse(time1).toString()
            }
            else {
                println("The input time is invalid")
                continue
            }
        } catch (e: Exception) {
            println("The input time is invalid")
            continue
        }
    }
}

private fun receivingListOfTask(): MutableList<String> {
    println("Input a new task (enter a blank line to end):")
    var size = 0
    val listOfTask: MutableList<String> = mutableListOf()
    while (true) {

        val taskOne: String = readln().trim()
        if (taskOne.isNotEmpty()) {
            listOfTask.add(taskOne)
            size++
        }
        else {
            if (size == 0) {
                println("The task is blank")
                break
            }
            else {
                break
            }
        }
    }
    return listOfTask
}

private fun printTasks() {
    println("+----+------------+-------+---+---+--------------------------------------------+\n| N  |    Date    | Time  | P | D |                   Task                     |\n+----+------------+-------+---+---+--------------------------------------------+")
    for (i in 0 until tasks.size) {
        val date = tasks[i].date
        var P = ""
        when (tasks[i].priority.uppercase()) {
            "C" -> P = "\u001B[101m \u001B[0m"
            "H" -> P = "\u001B[103m \u001B[0m"
            "N" -> P = "\u001B[102m \u001B[0m"
            "L" -> P = "\u001B[104m \u001B[0m"
        }
        var D = ""
        when (tasks[i].tag) {
            'I' -> D = "\u001B[102m \u001B[0m"
            'T' -> D = "\u001B[103m \u001B[0m"
            'O' -> D = "\u001B[101m \u001B[0m"
        }
        val time = tasks[i].time
        val task: MutableList<String> = printingTask(tasks[i].task)
        if (i < 9 ) println("| ${i + 1}  | $date | $time | $P | $D |${task.first()}|")
        else println("| ${i + 1} |$date|$time| $P | $D |${task[0]}|")
        if (task.size > 1) {
            for (i in 1 until task.size)
                println("|    |            |       |   |   |${task[i]}|")
        }
        println("+----+------------+-------+---+---+--------------------------------------------+")
    }
}

private fun printingTask(taskRec: MutableList<String>): MutableList<String> {
    val listIterator = taskRec.iterator()
    var size = 0
    val task: MutableList<String> = mutableListOf()
    var line = ""
    while (listIterator.hasNext()){
        var iterator = listIterator.next().iterator()
        while (iterator.hasNext()){
            line = line.plus(iterator.next())
            size++
            if (size == 44 || !iterator.hasNext()) {
                if (!iterator.hasNext()) {
                    while (line.length < 44) {
                        line += " "
                    }
                }
                task.add(line)
                size = 0
                line = ""
            }
        }
    }
    return task
}

private fun delete() {
    if (tasks.isEmpty()) println("No tasks have been input")
    else {
        tasks.removeAt(numberOfTask() - 1)
        println("The task is deleted")
    }
}

private fun edit() {
    if (tasks.isEmpty()) println("No tasks have been input")
    else {
        val number = numberOfTask() - 1
        var element: String
        while (true) {
            println("Input a field to edit (priority, date, time, task):")
            element = readln().trim()
            when (element) {
                "priority" -> {
                    tasks[number].priority = receivingPriority()
                    break
                }
                "date" -> {
                    tasks[number].date = receivingDate()
                    tasks[number].tag = receivingTag(tasks[number].date)
                    break
                }
                "time" -> {
                    tasks[number].time = receivingTime()
                    break
                }
                "task" -> {
                    tasks[number].task = receivingListOfTask()
                    break
                }
                else -> println("Invalid field")
            }
        }
        println("The task is changed")
    }
}

private fun numberOfTask(): Int {
    var number: Int
    while (true) {
        println("Input the task number (1-${tasks.size}):")
        try {
            number = readln().trim().toInt()
            if (number <= tasks.size && number > 0) break
            else println("Invalid task number")
        } catch (e: RuntimeException) {
            println("Invalid task number")
        }
    }
    return number
}

private fun save() {
    val lst = mutableListOf<Tmp>()
    for (task in tasks) lst.add(
        Tmp(
            task.priority,
            task.date.toString(),
            task.tag.toString(),
            task.time,
            task.task
        )
    )
    if (!jsonFile.exists()) jsonFile.createNewFile()
    jsonFile.appendText(taskAdapter.toJson(lst))
}

private fun fromJSON() {
    val text = jsonFile.readText()
    if (text.isNotEmpty()) {
        val list: List<Tmp?> = taskAdapter.fromJson(text)!!
        for (task in list) {
            tasks.add(
                Task(
                    priority = task!!.priority,
                    date = task.date.toLocalDate(),
                    tag = task.tag.toCharArray()[0],
                    time = task.time,
                    task = task.task.toMutableList()
                )
            )
        }
    }
}