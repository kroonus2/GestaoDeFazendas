package com.example.prova02_rafaelcaroni.TelasFazenda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prova02_rafaelcaroni.Classes.Fazenda
import com.example.prova02_rafaelcaroni.Controller.FazendaController
import com.example.prova02_rafaelcaroni.TelasFazenda.ui.theme.Prova02_RafaelCaroniTheme

class TelaFazendaInserir : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fazendaAlterar = intent.getParcelableExtra<Fazenda>("fazenda")
        setContent {
            Prova02_RafaelCaroniTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InserirFazenda(fazendaAlterar)
                }
            }
        }
    }
}


//Função abaixo aproveita os campos para se for passado uma fazenda como parametro, seja possivel atualiza-lá,
//se não insere uma nova fazenda;
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InserirFazenda(fazenda: Fazenda? = null) {
    val fieldCodigo: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue(fazenda?.codigo ?: "")) }
    val fieldNome: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue(fazenda?.nome ?: "")) }
    val fieldValorDaPropriedade: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue(fazenda?.valorDaPropriedade?.toString() ?: "")) }
    val fieldQntdFuncionarios: MutableState<TextFieldValue> =
        remember { mutableStateOf(TextFieldValue(fazenda?.qntdFuncionarios?.toString() ?: "")) }

    val fazendaController = FazendaController()
    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)


    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 5.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(50.dp, 0.dp)) {
            Text(
                text = "Gestão de Fazendas",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "By - Rafael Caroni", fontSize = 10.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = fieldCodigo.value, onValueChange = {
                    fieldCodigo.value = it
                },
                label = { Text(text = "Código ") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                enabled = fazenda == null //Se a fazenda for nula, o campo estará habilitado para inserção. Caso contrário, o campo estará desabilitado para edição.
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = fieldNome.value, onValueChange = {
                    fieldNome.value = it
                },
                label = { Text(text = "Nome") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = fieldValorDaPropriedade.value, onValueChange = {
                    fieldValorDaPropriedade.value = it
                },
                label = { Text(text = "Valor Da Propriedade") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = fieldQntdFuncionarios.value, onValueChange = {
                    fieldQntdFuncionarios.value = it
                },
                label = { Text(text = "Qntd de Funcionarios") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Magenta,
                    unfocusedBorderColor = Color(145, 89, 150, 255)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Card(
                onClick = {
                    val fazendaNova = Fazenda(
                        fieldCodigo.value.text,
                        fieldNome.value.text,
                        fieldValorDaPropriedade.value.text.toDouble(),
                        fieldQntdFuncionarios.value.text.toInt()
                    )
                    //Caso já exista fazenda, atualize-a
                    if (fazenda != null) {
                        // Atualizar fazenda existente
                        fazendaController.atualizarFazenda(fazendaNova, contexto) { sucesso ->
                            if (sucesso) {
                                Intent().apply {
                                    activity?.setResult(Activity.RESULT_OK, this)
                                }
                                activity?.finish()
                            } else {
                                activity?.finish()
                            }
                        }
                        //caso não exista fazenda, insira-a
                    } else {
                        // Inserir nova fazenda
                        fazendaController.inserirFazenda(fazendaNova, contexto) { sucesso ->
                            if (sucesso) {
                                fieldCodigo.value = TextFieldValue("")
                                fieldNome.value = TextFieldValue("")
                                fieldValorDaPropriedade.value = TextFieldValue("")
                                fieldQntdFuncionarios.value = TextFieldValue("")
                            } else {
                                fieldCodigo.value = TextFieldValue("")
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally),
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Adicionar Fazenda",
                        )
                    }
                    Text(text = "Adicionar Fazenda", modifier = Modifier.padding(12.dp))
                }
            }
        }
    }
}
