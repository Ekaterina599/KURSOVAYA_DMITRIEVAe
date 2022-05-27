import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection


//УСТАНОВКА СОЕДИНЕНИЯ С БД
class Mongo {
    private val client = KMongo.createClient("mongodb://root:7wXqiMyv9ivs@192.168.0.5:27017")
    private val mongoDatabase = client.getDatabase("Kyrsov")
    val students = mongoDatabase.getCollection<Student>()
    val tasks = mongoDatabase.getCollection<Tasks>()
}
val mongoDb = Mongo()
val tasks: ArrayList<Tasks> = ArrayList(mongoDb.tasks.find().toList())

//ДОБАВЛЯЕМ В БАЗУ ИНФ. О СТУДЕНТАХ
fun studentINFORNATION() {
    val studentINFO = Mongo()
    studentINFO.students.apply { drop() }
    val newStudents = listOf(
        Student("Сергей", "Пушкин", "Кириллович"),
        Student("Мария", "Дёмина", "Андреевна"),
        Student("Артем", "Гришин", "Андреевич"))
    studentINFO.students.insertMany(newStudents)
}



