package com.example.prova02_rafaelcaroni.Controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.prova02_rafaelcaroni.Classes.Fazenda
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FazendaController {

    private val refFazenda = Firebase.database.reference.child("Fazendas")

    //Função de inserir fazenda no firebase, verifica se o id existe no banco, se não existir insere o msm
    fun inserirFazenda(fazenda: Fazenda, contexto: Context, callback: (Boolean) -> Unit) {
        refFazenda.child(fazenda.codigo)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        refFazenda.child(fazenda.codigo).setValue(fazenda)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Fazenda Inserida Com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true)
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Inserir a fazenda",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false)
                            }
                    } else {
                        Toast.makeText(
                            contexto,
                            "Código ${fazenda.codigo} já inserido!",
                            Toast.LENGTH_LONG
                        ).show()
                        callback(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        contexto,
                        "Erro ao Inserir a fazenda",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false)
                }
            })
    }

    //Função de atualizar Fazenda no Banco, verifica se o id já existe, se existir altera o msm. Compartilha da mesma tela inserir, alterando a logica da pagina, comentarios no arquivo
    fun atualizarFazenda(fazenda: Fazenda, contexto: Context, callback: (Boolean) -> Unit) {
        refFazenda.child(fazenda.codigo)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        refFazenda.child(fazenda.codigo).setValue(fazenda)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    contexto,
                                    "Fazenda Atualizada Com Sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(true)
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    contexto,
                                    "Erro ao Atualizar a fazenda",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callback(false)
                            }
                    } else {
                        Toast.makeText(
                            contexto,
                            "Código ${fazenda.codigo} não encontrado!",
                            Toast.LENGTH_LONG
                        ).show()
                        callback(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        contexto,
                        "Erro ao Atualizar a fazenda",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false)
                }
            })
    }
    //Função de excluir fazenda, verifica se o id existe, se existir dela o msm do banco
    fun excluirFazenda(codigo: String, contexto: Context, callback: (Boolean) -> Unit) {
        refFazenda.child(codigo).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    refFazenda.child(codigo).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(
                                contexto,
                                "Fazenda Excluída com Sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(true)
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                contexto,
                                "Erro ao Excluir a Fazenda",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(false)
                        }
                } else {
                    Toast.makeText(
                        contexto,
                        "Código $codigo não encontrado!",
                        Toast.LENGTH_LONG
                    ).show()
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    contexto,
                    "Erro ao Excluir a Fazenda",
                    Toast.LENGTH_SHORT
                ).show()
                callback(false)
            }
        })
    }
    //Função de carregar a lista completa de fazenda, função assincrona, ou seja, recarrega dependendo de alterações no banco
    suspend fun carregarListaFazendas(): ArrayList<Fazenda> = suspendCoroutine { continuation ->
        val listaRetorno: ArrayList<Fazenda> = ArrayList()

        refFazenda.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val gson = Gson()

                    for (i in snapshot.children) {
                        val json = gson.toJson(i.value)
                        val fazenda = gson.fromJson(json, Fazenda::class.java)

                        listaRetorno.add(
                            Fazenda(
                                fazenda.codigo,
                                fazenda.nome,
                                fazenda.valorDaPropriedade,
                                fazenda.qntdFuncionarios
                            )
                        )
                    }
                    continuation.resume(listaRetorno)
                } else {
                    continuation.resumeWithException(Exception("Nenhuma Fazenda encontrada no banco de dados."))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        })
    }
    //Função de calcular media, pega o valor das propriedades de cada fazenda e soma, no final fazendo a media com um count iterado por cada fazenda passada
    fun calcularMediaValorPropriedades(callback: (Double?) -> Unit) {
        refFazenda.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalValorPropriedades = 0.0
                var totalFazendas = 0

                val gson = Gson()

                for (fazendaSnapshot in snapshot.children) {
                    val fazendaJson =
                        gson.toJson(fazendaSnapshot.value) // Converte o objeto para JSON
                    val fazenda = gson.fromJson(
                        fazendaJson,
                        Fazenda::class.java
                    ) // Converte o JSON de volta para o objeto Fazenda

                    fazenda?.let {
                        totalValorPropriedades += it.valorDaPropriedade
                        totalFazendas++
                    }
                }

                val mediaValorPropriedades = if (totalFazendas > 0) {
                    totalValorPropriedades / totalFazendas
                } else {
                    null
                }

                Log.d("Firebase", "Média de valor das propriedades: $mediaValorPropriedades")
                callback(mediaValorPropriedades)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao calcular média de valor das propriedades: $error")
                callback(null)
            }
        })
    }
    //Função de salvar os dados do firebase em um arquivo txt, arquivo este não lido durante o projeto, porém sendo possivel
    fun salvarDadosEmArquivoTxt(contexto: Context, callback: (Boolean) -> Unit) {
        refFazenda.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filesDir = contexto.filesDir
                val file = File(filesDir, "fazendas.txt")
                val fileOutputStream = FileOutputStream(file)
                val data = StringBuilder()

                val gson = Gson()

                for (fazendaSnapshot in snapshot.children) {
                    val fazendaJson =
                        gson.toJson(fazendaSnapshot.value) // Converte o objeto para JSON
                    val fazenda = gson.fromJson(
                        fazendaJson,
                        Fazenda::class.java
                    ) // Converte o JSON de volta para o objeto Fazenda

                    fazenda?.let {
                        data.append("Código: ${it.codigo}\n")
                        data.append("Nome: ${it.nome}\n")
                        data.append("Valor da Propriedade: ${it.valorDaPropriedade}\n")
                        data.append("Qtde Funcionários: ${it.qntdFuncionarios}\n")
                        data.append("\n")
                    }
                    Log.i("FazendaTxt", "${fazenda}")
                }

                fileOutputStream.write(data.toString().toByteArray())
                fileOutputStream.close()
                Log.d("Firebase", "Dados salvos em arquivo: ${file.absolutePath}")
                callback(true)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Erro ao salvar dados em arquivo: $error")
                callback(false)
            }
        })
    }
}
