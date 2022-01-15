package cinema

import java.math.RoundingMode

const val COST_SMALL_HALL = 10
const val COST_BIG_HALL = 8
const val SHIFT_PLACES = 60
const val PERCENTS = 100

fun main() {
    menu()
}

fun seatsScheme (rows: Int, columns: Int): MutableList<MutableList<String>> {
    val seats = mutableListOf<MutableList<String>>()

    for (i in 0..rows) {
        val oneRow = mutableListOf<String>()
        for (j in 0..columns) {
            when {
                i == 0 && j == 0 -> oneRow.add(" ")
                i == 0 -> oneRow.add("$j")
                j == 0 -> oneRow.add("$i")
                else -> oneRow.add("S")
            }
        }
        seats += oneRow
    }

    return seats
}

fun totalIncome (rows: Int, columns: Int): Int {
    return when {
        rows * columns > 60 -> {
            rows / 2 * columns * COST_SMALL_HALL + (rows - rows / 2) * columns * COST_BIG_HALL
        }
        else -> {
            rows * columns * COST_SMALL_HALL
        }
    }
}

fun printSeats (seats: MutableList<MutableList<String>>) {
    println("Cinema:")
    for (i in seats.indices) {
        println(seats[i].joinToString(" "))
    }
}

fun ticketPrice (seats: MutableList<MutableList<String>>, seatRow: Int, seatColumn: Int): Int {
    seats[seatRow][seatColumn] = "B"

    return when {
        seats.lastIndex * seats[0].lastIndex > SHIFT_PLACES && seatRow > seats.lastIndex / 2 -> {
            COST_BIG_HALL
        }
        else -> {
           COST_SMALL_HALL
        }
    }
}

fun printStatistic (purchaseTickets: Int, currentIncome: Int, totalIncome: Int, totalTickets: Int) {
    val percentage = (purchaseTickets.toDouble() / totalTickets.toDouble() * PERCENTS.toDouble())
    println("Number of purchased tickets: $purchaseTickets")
    println("Percentage: ${percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP)}%")
    println("Current income: \$$currentIncome")
    println("Total income: \$$totalIncome\n\n")
}

fun chooseAndBuy (seats: MutableList<MutableList<String>>): Int {
    loop@ while (true) {
        try {
            println("Enter a row number:")
            val seatRow = readLine()!!.toInt()
            println("Enter a seat number in that row:")
            val seatColumn = readLine()!!.toInt()

            if (seats[seatRow][seatColumn] == "B") {
                println("That ticket has already been purchased!\n\n")
                continue@loop
            } else {
                return ticketPrice(seats, seatRow, seatColumn)
            }
        } catch (e: Exception) {
            println("Wrong input!\n\n")
            continue@loop
        }
    }
}
fun menu () {

    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()

    println("Enter the number of seats in each row:")
    val columns = readLine()!!.toInt()

    val seats = seatsScheme(rows, columns)
    val totalIncome = totalIncome(rows, columns)
    val totalTickets = rows * columns
    var currentIncome = 0
    var purchaseTickets = 0

    while (true) {
        println("1. Show the seats\n" +
                "2. Buy a ticket\n" +
                "3. Statistics\n" +
                "0. Exit"
        )

        when (readLine()!!) {
            "0" -> return
            "1" -> printSeats(seats)
            "2" -> {
                val ticketPrice = chooseAndBuy(seats)
                println("Ticket price: \$$ticketPrice\n\n")
                currentIncome += ticketPrice
                purchaseTickets ++
            }
            "3" -> printStatistic(purchaseTickets, currentIncome, totalIncome, totalTickets)
        }
    }
}