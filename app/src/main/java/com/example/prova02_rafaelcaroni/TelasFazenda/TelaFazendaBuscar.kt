package com.example.prova02_rafaelcaroni.TelasFazenda

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.prova02_rafaelcaroni.Classes.CampoBusca
import com.example.prova02_rafaelcaroni.Classes.Fazenda
import com.example.prova02_rafaelcaroni.Controller.FazendaController
import com.example.prova02_rafaelcaroni.TelasFazenda.ui.theme.Prova02_RafaelCaroniTheme
import kotlinx.coroutines.launch

class TelaFazendaBuscar : ComponentActivity() {
    lateinit var listaFazendas: ArrayList<Fazenda>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fazendaController = FazendaController()
        lifecycleScope.launch {
            try {
                listaFazendas = fazendaController.carregarListaFazendas()
                setContent {
                    Prova02_RafaelCaroniTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            BuscarFazenda(listaFazendas)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.i("errorCarregarListaFazendas", "${e}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BuscarFazenda(listaFazendas: ArrayList<Fazenda>) {
    val searchQuery: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue()) }
    var selectedCampoBusca by remember { mutableStateOf(CampoBusca.CODIGO) }
    val contexto: Context = LocalContext.current
    var fazendaExistente by remember { mutableStateOf(false) }
    var fazendaEncontrada: Fazenda? = null
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 5.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
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
            Card(
                modifier = Modifier
                    .padding(10.dp, 10.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedCampoBusca == CampoBusca.CODIGO,
                        onClick = { selectedCampoBusca = CampoBusca.CODIGO },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(text = "Código")
                    RadioButton(
                        selected = selectedCampoBusca == CampoBusca.NOME,
                        onClick = { selectedCampoBusca = CampoBusca.NOME },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(text = "Nome")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(30.dp, 5.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery.value,
                        onValueChange = {
                            searchQuery.value = it
                        },
                        label = { Text(text = "Parâmetro de Busca") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Magenta,
                            unfocusedBorderColor = Color(145, 89, 150, 255)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Button(
                        onClick = {
                            val query = searchQuery.value.text.trim()
                            if (query.isNotEmpty()) {
                                fazendaEncontrada = when (selectedCampoBusca) {
                                    CampoBusca.CODIGO -> listaFazendas.find { it.codigo == query }
                                    CampoBusca.NOME -> listaFazendas.find { it.nome == query }
                                }
                                if (fazendaEncontrada != null) {
                                    fazendaExistente = true
                                    Log.i("FazendaBuscado", "$fazendaEncontrada")
                                } else {
                                    Toast.makeText(
                                        contexto,
                                        "Fazenda com ${selectedCampoBusca.name}: $query não encontrada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    fazendaExistente = false
                                }
                            } else {
                                Toast.makeText(
                                    contexto,
                                    "Por favor, insira um valor para buscar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        },
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .align(Alignment.CenterHorizontally),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "Buscar")
                    }
                }
            }
        }
        if (fazendaExistente) {
            fazendaEncontrada?.let { fazenda ->
                Card(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp, 8.dp)) {
                        Text(
                            text = "${fazenda.nome}",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(2.dp)
                        )
                        Text(
                            text = "Codigo: ${fazenda.codigo}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Valor da Propriedade: R$${fazenda.valorDaPropriedade}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Qntd de Funcionários: ${fazenda.qntdFuncionarios}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
