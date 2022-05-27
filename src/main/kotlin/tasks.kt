import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*
import org.litote.kmongo.*


@Serializable
class Tasks (
    var deadline: String?, //Дата
    var type: Int?, //тип
    var name: String, //наименование
    var description: String, //описание
    var maxGrade: Int, //максимальный балл
    @Contextual val id: Id<Tasks> = newId() // Идентификатор задания
)
{
    val completedTasks: ArrayList<Result> = arrayListOf() // Список выполненых заданий

    //ИНФОРМАЦИЯ О ЗАДАНИИ
    fun INFOtasks(): String {
        val name = "\n\nНазвание задания: $name \n" +
                "Описание задания: $description \n" +
                "Максимальная оценка: $maxGrade \n" +
                "Дата сдачи: $deadline\n"
        return name
    }

    //ДЕЙСТВИЯ С ЗАДАНИЕМ(ИЗЕМЕНЕНИЕ+УДАЛЕНИЕ+ПРОСМОТР ВЫПОЛНЕННЫХ)
    fun SCANtasks() {
        println("\nВыберите действие(нажмите соответсвующую цифру):\n" +
                "1 -> Внести изменения в задание\n" +
                "2 -> Действия с выполненными задания\n" +
                "3 -> Удалить задание\n" +
                "4 -> Вернуться в главное меню")
        when (readLine()!!.toInt()) {
            1 -> changeCRITERIA()

            2 -> {
                // Для каждого выполненого задания
                println("1 -> Посмотреть и редактировать выполненное задание")
                println("2 -> Вернуться в меню редактирования заданий")
                val number: Int = readLine()!!.toInt()
                when (number) {
                    in 1..completedTasks.size -> { completedTasks[number - 1].EXECUTIONresult() }
                    1 -> ADDcompletedTASKS()
                    2 -> SCANtasks()
                    else -> { println("\n!!! Введен неверный номер !!!")
                        SCANtasks()
                    }
                }
            }

            3 -> {
                tasks.remove(this) //Удаляем из списка заданий
                mongoDb.tasks.deleteOne(Tasks::id eq id) //Удаляем из бд
                println ("\nЗадание успешно удалено!")
            }

            4 -> return
            else -> {
                println("\n!!! Выбран неверный номер действия !!!")
                SCANtasks()
            }
        }
    }

    //ИЗМЕНИЕ КРИТЕРИЕВ ЗАДАНИЯ
    private fun changeCRITERIA() {
        println("\n\nВыберите действие:")
        println("1. Изменить название задания")
        println("2. Изменить описание")
        println("3. Изменить максимальный балл")
        println("4. Изменить дату сдачи")
        println("5. Вернуться назад")
        when (readLine()!!.toInt()) {
            1 -> {
                print("\n\nВведите новое название задания: ")
                val newName = readLine().toString()
                mongoDb.tasks.updateOne(Tasks::id eq id, setValue(Tasks::name, newName))
                name = newName
                println("\nНазвание задания изменено на \"$newName\"")
                changeCRITERIA()
            }

            2 -> {
                print("\n\nВведите новое описание: ")
                val newDescription = readLine().toString()
                mongoDb.tasks.updateOne(Tasks::id eq id, setValue(Tasks::description, newDescription))
                description = newDescription
                println("\nОписание задания изменено на \"$newDescription\"")
                changeCRITERIA()
            }

            3 -> {
                print("\n\nВведите маскимальное количетсво баллов: ")
                val newGrade: Int = readLine()!!.toInt()
                mongoDb.tasks.updateOne(Tasks::id eq id, setValue(Tasks::maxGrade, newGrade))
                maxGrade = newGrade
                println("\nМаксимальная оценка изменена на \"$newGrade\"")
                changeCRITERIA()
            }

            4 -> {
                print("\n\nВведите новую дату: ")
                val newDates: String = readLine().toString()
                mongoDb.tasks.updateOne(Tasks::id eq id, setValue(Tasks::deadline, newDates))
                deadline = newDates
                println("\nМаксимальная оценка изменена на \"$newDates\"")
                changeCRITERIA()
            }


            5 -> SCANtasks()
            else -> {
                println("\n!!! Выбран неверный номер действия !!!")
                changeCRITERIA()
            }
        }
    }

    //ДОБАВЛЕНИЕ ВЫПОЛНЕННОГО ЗАДАНИЯ
    private fun ADDcompletedTASKS() {
        val students = mongoDb.students.find().toList()

        var number = 1
        println("Выберите студента из списка:")
        //Список студентов
        for (student in students) {
            println("$number. ${student.names}")
            number++ }
        //Выбор студента из списка
        val count = readLine()!!.toInt() 
        val selectedStudent: Student =
            if (count in 1..students.size) {
                students[count - 1] }
            else return

        print("Введите полученную оценку (МАКСИМАЛЬНАЯ ОЦЕНКА - $maxGrade): ")
        var GradeStudent: Int = readLine()!!.toInt()
        if (GradeStudent > maxGrade) {
            println("Введена оценка, превышающая допустимую")
            GradeStudent = maxGrade
        }

        print("Введите дату сдачи задания: ")
        val DateStudent = readLine() ?: "00.00.2000"

        print("Введите примечания к заданию: ")
        val notesTasks = readLine().toString()

        val newCompletedTask = Result( DateStudent, id, selectedStudent, GradeStudent, notesTasks)

        completedTasks.add(newCompletedTask) //В список результатов записываетя новый результат выполнения задания
        mongoDb.tasks.updateOne( //В бд обновляется ин-фя о результах выполнения задания
            Tasks::id eq id,
            setValue(Tasks::completedTasks, completedTasks))

        println("\nРЕЗУЛЬТАТ выполнения задания добавлен!")
        SCANtasks()
    }
}


