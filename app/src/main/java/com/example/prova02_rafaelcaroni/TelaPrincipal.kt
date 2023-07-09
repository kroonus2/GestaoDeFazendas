package com.example.prova02_rafaelcaroni

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prova02_rafaelcaroni.Controller.FazendaController
import com.example.prova02_rafaelcaroni.TelasFazenda.TelaFazendaBuscar
import com.example.prova02_rafaelcaroni.TelasFazenda.TelaFazendaInserir
import com.example.prova02_rafaelcaroni.TelasFazenda.TelaFazendaListar
import com.example.prova02_rafaelcaroni.ui.theme.Prova02_RafaelCaroniTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Prova02_RafaelCaroniTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    telaPrincipal()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun telaPrincipal() {
    val contexto: Context = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)

    val fazendaController = FazendaController()

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
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaFazendaInserir::class.java
                        )
                    )
                },
                modifier = Modifier
                    .padding(100.dp, 10.dp)
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
                    Text(text = "Adicionar Fazendas", modifier = Modifier.padding(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaFazendaListar::class.java
                        )
                    )
                },
                modifier = Modifier
                    .padding(100.dp, 10.dp)
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
                            imageVector = Icons.Filled.List,
                            contentDescription = "Listar Fazendas",
                        )
                    }
                    Text(text = "Listar Fazendas", modifier = Modifier.padding(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                onClick = {
                    contexto.startActivity(
                        Intent(
                            contexto,
                            TelaFazendaBuscar::class.java
                        )
                    )
                },
                modifier = Modifier
                    .padding(100.dp, 10.dp)
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
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar fazenda especifica",
                        )
                    }
                    Text(text = "Buscar Fazenda", modifier = Modifier.padding(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                onClick = {
                    fazendaController.salvarDadosEmArquivoTxt(contexto) { sucesso ->
                        if (sucesso) {
                            Toast.makeText(
                                contexto,
                                "Dados baixados e Salvos em arquivo txt",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                contexto,
                                "Erro ao baixar dados e salvar em arquivo txt",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .padding(100.dp, 10.dp)
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
                            imageVector = Icons.Filled.Done,
                            contentDescription = "salvar fazendas em txt",
                        )
                    }
                    Text(text = "Salvar Fazendas '.txt'", modifier = Modifier.padding(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                onClick = {
                    fazendaController.calcularMediaValorPropriedades { valorMedio ->
                        if (valorMedio != null) {
                            Toast.makeText(
                                contexto,
                                "Valor Médio das Propriedades é de RS$valorMedio",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                contexto,
                                "Erro ao Calcular Valor Médio",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .padding(100.dp, 10.dp)
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
                            imageVector = Icons.Outlined.PlayArrow,
                            contentDescription = "mostrar media de valor",
                        )
                    }
                    Text(text = "Média de Valor", modifier = Modifier.padding(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

