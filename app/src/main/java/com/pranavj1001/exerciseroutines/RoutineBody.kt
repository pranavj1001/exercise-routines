package com.pranavj1001.exerciseroutines

class RoutineBody() {
    var name: String = ""
    var time: String = ""
    var exercises: Array<ExerciseBody> = emptyArray()
}

class ExerciseBody() {
    var name: String = ""
    var time: String = ""
}

class Time() {
    var hours: String = ""
    var minutes: String = ""
    var seconds: String = ""
}