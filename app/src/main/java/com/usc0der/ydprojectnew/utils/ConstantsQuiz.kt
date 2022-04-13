package com.usc0der.ydprojectnew.utils

import com.usc0der.ydprojectnew.model.Question
import com.usc0der.ydprojectnew.model.Test

object ConstantsQuiz {

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        val question1 = Question(
            1,
            "Quydagi javoblardan qaysi birida 3 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            3
        )
        questionsList.add(question1)

        val question2 = Question(
            1,
            "Quydagi javoblardan qaysi birida 2 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            2
        )
        questionsList.add(question2)

        val question3 = Question(
            1,
            "Quydagi javoblardan qaysi birida 1 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            1
        )
        questionsList.add(question3)

        val question4 = Question(
            1,
            "Quydagi javoblardan qaysi birida 4 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            4
        )
        questionsList.add(question4)

        val question5 = Question(
            1,
            "Quydagi javoblardan qaysi birida 1 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            1
        )
        questionsList.add(question5)

        val question6 = Question(
            1,
            "Quydagi javoblardan qaysi birida 4 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            4
        )
        questionsList.add(question6)

        val question7 = Question(
            1,
            "Quydagi javoblardan qaysi birida 3 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            3
        )
        questionsList.add(question7)

        val question8 = Question(
            1,
            "Quydagi javoblardan qaysi birida 1 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            1
        )
        questionsList.add(question8)

        val question9 = Question(
            1,
            "Quydagi javoblardan qaysi birida 4 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            4
        )
        questionsList.add(question9)

        val question10 = Question(
            1,
            "Quydagi qaysi birida 2 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            2
        )
        questionsList.add(question10)
        val question11 = Question(
            1,
            "Quydagi qaysi birida 3 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            3
        )
        questionsList.add(question11)

        val question12 = Question(
            1,
            "Quydagi qaysi birida 3 soni ko`rsatilgan",
            "1",
            "2",
            "3",
            "4",
            3
        )
        questionsList.add(question12)
        return questionsList
    }


}