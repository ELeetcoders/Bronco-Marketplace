package com.eleetcoders.api.services

class NameComparator : Comparator<Map<String, Any>> {
    override fun compare(m1: Map<String, Any>, m2: Map<String, Any>): Int {
        return m1["name"].toString().compareTo(m2["name"].toString())
    }
}

class PriceComparator : Comparator<Map<String, Any>> {
    override fun compare(m1: Map<String, Any>, m2: Map<String, Any>): Int {
        val m1Price = m1["price"].toString().toInt()
        val m2Price = m2["price"].toString().toInt()

        return when {
            m1Price < m2Price -> -1
            m1Price == m2Price -> 0
            else -> 1
        }
    }
}