package com.example.prova02_rafaelcaroni.Classes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fazenda(
    val codigo: String,
    val nome: String,
    val valorDaPropriedade: Double,
    val qntdFuncionarios: Int
) : Parcelable
