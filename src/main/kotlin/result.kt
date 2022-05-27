import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.*

@Serializable
class Student (private val name: String, private val surname: String,private val patronymic: String )
{
    val names: String
        get() = "$name $surname $patronymic"
}

@Serializable
class Result (
    val date: String, //Дата
    @Contextual val id: Id<Tasks> = newId(), //Идентификатор
    val student: Student, //ФИО студента
    val grades: Int, //Оценка
    val notes: String, //Примечания
){

    //РЕЗУЛЬТАТ ВЫПОЛНЕНИЯ ЗАДАНИЯ СТУДЕНТАМИ
    fun EXECUTIONresult() {
    println("\nВыполнил студент: ${student.names}\n"+
            "Дата сдачи: ${date}\n" +
            "Оценка: $grades\n")

    println("Выберите действие:\n" +
            "1. Изменить балл, полученный студентом\n" +
            "2. Удалить результат выполнения задания\n" +
            "3. Вернуться к предыдущему меню действий")
    when (readLine()!!.toInt() ) {
        1 -> {
            print("\n\nВведите новое количетсво баллов: ")
            val newGrade: Int = readLine()!!.toInt()
            if (newGrade > mongoDb.tasks.findOne(Tasks::id eq id)!!.maxGrade) {
                println("\n!!! ВВЕДИТЕ ОЦЕНКУ НЕ ПРЕВЫЩАЮЩУЮ МАХ КОЛ-ВО БАЛЛОВ!!!") }
            else {
                mongoDb.tasks.updateOne(
                    and(Tasks::id eq id, Tasks::completedTasks / Result::student eq student),
                    setValue(Tasks::completedTasks.posOp / Result::grades, newGrade)
                )
                println("\nОценка изменена")
            }
            EXECUTIONresult()
        }
        2 -> {
            val task = tasks.find { it.id == id }
            task!!.completedTasks.remove(this)
            mongoDb.tasks.deleteOne(
                and(Tasks::id eq id, Tasks::completedTasks / Result::student eq student)
            )
            println("\nРезультат выполнения задания удален")
            task.SCANtasks()
        }
        3 -> tasks.find { it.id == id }!!.SCANtasks()
        else -> println("\n!!! Выбран неверный номер действия !!!")
    }
    }
}


