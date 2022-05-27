
val scan = java.util.Scanner(System. `in`)
//ДОБАВЛЕНИЕ ДАННЫХ ЗАДАНИЯ
fun addDATE(): String {
    println("Введите дедлайн(дату сдачи) задания в формате (dd.MM.yyyy)")
    var date = readLine() ?: "01.01.2000"
    var output = true
    while(output) {
        if (date.isBlank()) {
            println("Ошибка. Возможно вы забыли указать наименование задания.\n" +
                    "Введите наименование задания")
            date = readLine() ?: "01.01.2000"
        }  else {
            output = false
        }
    }
    return date
}
fun addNAME(): String {
    println("Введите наименование задания")
    var name = scan.nextLine().toLowerCase()
    var output = true
    while(output) {
        if (name.isBlank()) {
            println("Ошибка. Возможно вы забыли указать наименование задания.\n" +
                    "Введите наименование задания")
            name = scan.nextLine().toLowerCase()
        }  else {
            output = false
        }
    }
    return name
}
fun addTYPE(): Int? {
    println("Введите тип задания")
    var Type: Int?
    do {
        println("1: Лабораторная")
        println("2: Лекционное задание")
        println("3: Практическое задание")
        Type = readLine()?.toIntOrNull()
    } while (Type !in 1..3)
       return Type
}
fun addDESCRIPTION(): String {
    println("Введите описание задания")
    var description = scan.nextLine().toLowerCase()
    var output = true
    while(output) {
        if (description.isBlank()) {
            println("Ошибка. Возможно вы забыли добавить описание.\n" +
                    "Добавьте описание")
            description = scan.nextLine().toLowerCase()
        }  else {
            output = false
        }
    }
    return description
}
fun addMAXGRADE(): Int {
    println("Введите МАКСИМАЛЬНЫЙ БАЛЛ ЗА ЗАДАНИЕ")
    var maxGrades = readLine()?.toIntOrNull() ?: 0
    return maxGrades
}

// Добавление задания
fun ADDTASKS() {
    val newTask = Tasks(
        addDATE(), addTYPE(), addNAME(), addDESCRIPTION(), addMAXGRADE())
    mongoDb.tasks.insertOne(newTask) // Добавление нового задания в базу данных
    tasks.add(newTask) // Добавление нового задания в общий список заданий
    println("\nЗадание успешно добавлено!")
}

//Просмотреть весь список заданий
fun ALLTASKS() {
    println("\n\nВыберите задания из списка:")
    //Вывод всех заданий с номером
    var NUMBERtasks = 1
    for (task in tasks) {
        println("Номер $NUMBERtasks ЗАДАНИЕ: ${task.INFOtasks()}\n")
        NUMBERtasks++
    }
    println("$NUMBERtasks -> Возврат в основное меню")

    //При выборе соотсвующего номера задания выводится
    val number: Int = readLine()?.toIntOrNull() ?: 0
    when (number) {
        in 1..tasks.size -> { tasks[number - 1].SCANtasks() }
        NUMBERtasks -> return
        else -> {
            println("\n! Введен неверный номер задания. Повторите попытку! ")
            ALLTASKS()
        }
    }

}

//ПОИСК ПО КАКОМУ-ЛИБО КРИТЕРИЮ
fun FINDTASKS() {
    println("\n\nВыберите КРИТЕРИЙ ЗАДАНИЙ для поиска:")
    println("1 -> По наименованию задания")
    println("2 -> Вернуться в главное меню\n")
    when (readLine()?.toIntOrNull()) {
        1 -> criterionNAME()
        2 -> return
        else -> {
            println("\n!!! Выбран неверный номер действия !!!")
            FINDTASKS()
        }
    }
}

//ПОИСК ПО КРИТЕРИЮ: НАЗВАНИЕ ЗАДАНИЯ
fun criterionNAME() { // 
    print("\n\nВведите наименование задания: ")
    val CRITERIONname = readLine() ?: ""
    val resultFind = tasks.filter { it.name == CRITERIONname}
    //ВЫДАЕМ ПО КРИТЕРИЮ ПОИСКА СООТВЕТСТВУЮЩИЙ РЕЗУЛЬТАТ
    if (resultFind.isNotEmpty()) {
        println("\n\nПо вашему критерию найдено задание:")
        var number = 1
        for (task in resultFind) {
            println("$number ЗАДАНИЕ: ${task.INFOtasks()}")
            number++
        }
        return
    }
    else {
        println("\n Нет задания с таким названием ")
        criterionNAME()
    }
}



